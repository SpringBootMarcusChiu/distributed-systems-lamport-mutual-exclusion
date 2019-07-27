package com.marcuschiu.example.spring.boot.mastercodesnippet.service;

import com.marcuschiu.example.spring.boot.mastercodesnippet.configuration.Config;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.ReleaseMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.ReplyMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.RequestMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.util.RequestComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class EventService {

    @Autowired
    Config config;

    @Value("${node.id}")
    Integer nodeID;

    @Autowired
    RestTemplate restTemplate;

    private final ReentrantLock reLock = new ReentrantLock(true);

    // dynamic variables (changes during runtime)
    private volatile AtomicInteger lamportTimestamp = new AtomicInteger(0);
    private volatile AtomicInteger numRepliesReceived = new AtomicInteger(0);
    private volatile PriorityQueue<RequestMessage> requestQueue = new PriorityQueue<>(10, new RequestComparator());

    @Async
    public void process(RequestMessage message) {
        reLock.lock();
        try {
            updateAndGetLTS(message.getTimestamp());
            requestQueue.add(message);
            sendReply(message.getFromID());
        } finally {
            reLock.unlock();
        }
    }
    private void sendReply(Integer toID) {
        // update LTS and send reply message to "toID"
        restTemplate.postForObject(
                config.getConfigNodeInfos().get(toID).getNodeURL() + "/message/reply",
                new ReplyMessage(updateAndGetLTS(null), nodeID),
                String.class);
    }

    @Async
    public void process(ReplyMessage message) {
        reLock.lock();
        try {
            updateAndGetLTS(message.getTimestamp());
            numRepliesReceived.incrementAndGet();
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void process(ReleaseMessage message) {
        reLock.lock();
        try {
            updateAndGetLTS(message.getTimestamp());
            requestQueue.poll();
        } finally {
            reLock.unlock();
        }
    }

    public void cs_enter() {
        reLock.lock();
        try {
            // update LTS once
            RequestMessage message = new RequestMessage(updateAndGetLTS(null), nodeID);

            // queue - add
            requestQueue.add(message);

            // broadcast REQUEST messages (except itself)
            for (int i = 0; i < config.getNumNodes(); i++) {
                if (!nodeID.equals(i)) {
                    restTemplate.postForObject(
                            config.getConfigNodeInfos().get(i).getNodeURL() + "/message/request",
                            message,
                            String.class);
                }
            }
        } finally {
            reLock.unlock();
        }

        while (numRepliesReceived.get() != (config.getNumNodes() - 1) &&
                !requestQueue.peek().getFromID().equals(nodeID)) {
            // do nothing
        }

        numRepliesReceived.set(0);
        // application can now enter critical-section
    }

    public void cs_leave() {
        reLock.lock();
        try {
            // update LTS once
            ReleaseMessage message = new ReleaseMessage(updateAndGetLTS(null), nodeID);

            // queue - pop
            requestQueue.poll();

            // broadcast RELEASE messages (except itself)
            for (int i = 0; i < config.getNumNodes(); i++) {
                if (i != nodeID) {
                    restTemplate.postForObject(
                            config.getConfigNodeInfos().get(i).getNodeURL() + "/message/release",
                            message,
                            String.class);
                }
            }
        } finally {
            reLock.unlock();
        }
    }

    synchronized Integer updateAndGetLTS(Integer pigLTS) {
        if (pigLTS != null) {
            lamportTimestamp.set(Math.max(pigLTS, lamportTimestamp.get()) + 1);
        } else {
            lamportTimestamp.incrementAndGet();
        }
        return lamportTimestamp.get();
    }
}

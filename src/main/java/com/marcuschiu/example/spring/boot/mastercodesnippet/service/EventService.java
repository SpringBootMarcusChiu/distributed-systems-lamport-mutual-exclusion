package com.marcuschiu.example.spring.boot.mastercodesnippet.service;

import com.marcuschiu.example.spring.boot.mastercodesnippet.model.ReleaseMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.ReplyMessage;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.RequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class EventService {

    // better than synchronized (intrinsic lock)
    // checks the queued threads and gives priority access to the longest waiting one
    private final ReentrantLock reLock = new ReentrantLock(true);

    @Autowired
    LamportService lamportService;

    @Async
    public void process(RequestMessage message) {
        reLock.lock();
        try {
            // do something
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void process(ReplyMessage message) {
        reLock.lock();
        try {
            // do something
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void process(ReleaseMessage message) {
        reLock.lock();
        try {
            // do something
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void send(RequestMessage message) {
        reLock.lock();
        try {
            // do something
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void send(ReplyMessage message) {
        reLock.lock();
        try {
            // do something
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void send(ReleaseMessage message) {
        reLock.lock();
        try {
            // do something
        } finally {
            reLock.unlock();
        }
    }
}

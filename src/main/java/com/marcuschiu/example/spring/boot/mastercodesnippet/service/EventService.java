package com.marcuschiu.example.spring.boot.mastercodesnippet.service;

import com.marcuschiu.example.spring.boot.mastercodesnippet.model.AppMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class EventService {

    // better than synchronized (intrinsic lock)
    // checks the queued threads and gives priority access to the longest waiting one
    private final ReentrantLock reLock = new ReentrantLock(true);

    @Async
    public void sendAppMessage(Integer toNodeID) {
        reLock.lock();
        try {
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void process(AppMessage appMessage) {
        reLock.lock();
        try {

        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void process() {
        reLock.lock();
        try {
        } finally {
            reLock.unlock();
        }
    }

    @Async
    public void selfInitiateSnapshot() {
    }
}

package com.epam.jmp.module.concurency;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;


public class Experiment  {

    AtomicInteger failsCount = new AtomicInteger(0);

    private void insert(Map<Integer, String> map) {
        map.put(ThreadLocalRandom.current().nextInt(), UUID.randomUUID().toString());
    }

    private int sum(Map<Integer, String> map) {
        return map.keySet().stream().mapToInt(k -> k).sum();
    }

    public void modify(Map<Integer, String> map) {
        Thread first = new Thread(() -> {
            try {
                insert(map);
            } catch (ConcurrentModificationException e) {
                failsCount.getAndIncrement();
                throw e;
            }
        });
        first.start();
        Thread second = new Thread(() -> {
            try {
                sum(map);
            } catch (ConcurrentModificationException e) {
                failsCount.getAndIncrement();
            }
        });
        second.start();
    }

    public AtomicInteger getFailsCount() {
        return failsCount;
    }

    public void setFailsCount(AtomicInteger failsCount) {
        this.failsCount = failsCount;
    }
}

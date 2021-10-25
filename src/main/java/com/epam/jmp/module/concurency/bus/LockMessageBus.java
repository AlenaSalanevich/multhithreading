package com.epam.jmp.module.concurency.bus;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockMessageBus<T, M> implements MessageBus<T, M> {

    private final Map<T, Queue<M>> content = new HashMap<>();

    private final Lock lock = new ReentrantLock();

    @Override
    public void push(T topic, M message) {
        lock.lock();
        try {
            content.computeIfAbsent(topic, ms -> new PriorityQueue<>()).add(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public M pop(T topic) {
        M message = null;
        lock.lock();
        try {
            message = Optional.ofNullable(content.get(topic)).orElse(new PriorityQueue<>()).poll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return message;
    }

    public Map<T, Queue<M>> getContent() {
        return content;
    }
}

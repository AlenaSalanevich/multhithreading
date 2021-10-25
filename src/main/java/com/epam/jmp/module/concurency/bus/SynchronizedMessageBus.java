package com.epam.jmp.module.concurency.bus;

import java.util.*;

public class SynchronizedMessageBus<T, M> implements MessageBus<T, M> {

    private final Map<T, Queue<M>> content = new HashMap<>();

    @Override
    synchronized public void push(T topic, M message) {
        content.computeIfAbsent(topic, ms -> new PriorityQueue<>()).add(message);
    }

    @Override
    synchronized public M pop(T topic) {
        return Optional.ofNullable(content.get(topic)).orElse(new PriorityQueue<>()).poll();
    }

    public Map<T, Queue<M>> getContent() {
        return content;
    }
}

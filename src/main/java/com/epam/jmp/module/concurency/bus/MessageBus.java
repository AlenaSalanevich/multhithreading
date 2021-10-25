package com.epam.jmp.module.concurency.bus;

public interface MessageBus<T, M> {

    M pop(T topic);

    void push(T topic, M message);
}

package com.epam.jmp.module.concurency.bus;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerImpl implements Producer, Runnable {

    private final MessageBus<String, String> bus;

    public ProducerImpl(MessageBus<String, String> bus) {
        this.bus = bus;
    }

    @Override
    public void send() {
        long l = System.currentTimeMillis();
        while (l + 100000 > System.currentTimeMillis()) {
            bus.push("message" + ThreadLocalRandom.current().nextInt(0, 100), UUID.randomUUID().toString());
        }
    }

    @Override
    public void run() {
        send();
    }
}

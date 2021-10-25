package com.epam.jmp.module.concurency.bus;

import java.util.concurrent.ThreadLocalRandom;

public class ConsumerImpl implements Consumer,Runnable {

    private final MessageBus<String, String> bus;

    public ConsumerImpl(MessageBus<String, String> bus) {
        this.bus = bus;
    }

    @Override
    public String receive() {
        return
                bus.pop("message" + ThreadLocalRandom.current().nextInt(0, 100));
    }

    @Override
    public void run() {
      while (true)
        receive();
    }
}

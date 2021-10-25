package com.epam.jmp.module.concurency;

import com.epam.jmp.module.concurency.bus.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.testng.Assert.assertNotNull;


public class MessageBusTest {

    private final SynchronizedMessageBus<String, String> bus = new SynchronizedMessageBus<>();
    private final LockMessageBus<String, String> lockBus = new LockMessageBus<>();

    @BeforeMethod
    public void setUp() {
    }


    @Test
    public void test() throws InterruptedException {
        Producer producer = new ProducerImpl(bus);
        Consumer consumer = new ConsumerImpl(bus);
        Executors.newSingleThreadExecutor().execute(producer::send);
        Executors.newSingleThreadExecutor().execute(producer::send);
        Thread.sleep(1000);
        Executors.newSingleThreadExecutor().execute(() -> IntStream.range(0, 100).forEach(i -> assertNotNull(consumer.receive())));
        Executors.newSingleThreadExecutor().execute(() -> IntStream.range(0, 100).forEach(i -> assertNotNull(consumer.receive())));
        assertNotNull(consumer.receive());
    }

    @Test
    public void testLock() throws InterruptedException {
        Producer producer = new ProducerImpl(lockBus);
        Consumer consumer = new ConsumerImpl(lockBus);
        ExecutorService executor1 = Executors.newSingleThreadExecutor();
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        executor1.execute(producer::send);
        executor2.execute(producer::send);
        Thread.sleep(1000);
        ExecutorService executor3 = Executors.newSingleThreadExecutor();
        ExecutorService executor4 = Executors.newSingleThreadExecutor();
        executor3.execute(() -> IntStream.range(0, 100).forEach(i -> assertNotNull(consumer.receive())));
        executor4.execute(() -> IntStream.range(0, 100).forEach(i -> assertNotNull(consumer.receive())));
        assertNotNull(consumer.receive());
    }
}
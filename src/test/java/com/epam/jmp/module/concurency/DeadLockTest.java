package com.epam.jmp.module.concurency;

import com.epam.jmp.module.concurency.bus.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.testng.Assert.assertNotNull;


public class DeadLockTest {

    @BeforeMethod
    public void setUp() {
    }


    @Test
    public void deadlockTest() throws InterruptedException {
        List<Integer> numbers = new CopyOnWriteArrayList<>();
        final AtomicBoolean isActive = new AtomicBoolean(true);
        List<Thread> threads = Arrays.asList(new Thread(() -> {
                    while (isActive.get()) {
                        numbers.add(ThreadLocalRandom.current().nextInt(0, 10000));
                    }
                }), new Thread(() -> {
                    while (isActive.get()) {
                        System.out.println(numbers.stream().reduce(0, Integer::sum));
                    }
                }),
                new Thread(() -> {
                    while (isActive.get()) {
                        System.out.println(numbers.stream().reduce(0, (x, y) ->
                                ((Double) (Math.sqrt(x) + Math.sqrt(y))).intValue()));
                    }
                }));
        threads.forEach(Thread::start);
        TimeUnit.MILLISECONDS.sleep(1000);
        isActive.set(false);
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    public void deadlockTestSynchronized() throws InterruptedException {
        List<Integer> numbers = new ArrayList<>();
        final AtomicBoolean isActive = new AtomicBoolean(true);
        List<Thread> threads = Arrays.asList(new Thread(() -> {
                    while (isActive.get()) {
                        synchronized (numbers) {
                            numbers.add(ThreadLocalRandom.current().nextInt(0, 10000));
                        }
                    }
                }), new Thread(() -> {
                    while (isActive.get()) {
                        synchronized (numbers) {
                            System.out.println(numbers.stream().reduce(0, Integer::sum));
                        }
                    }
                }),
                new Thread(() -> {
                    while (isActive.get()) {
                        synchronized (numbers) {
                            System.out.println(numbers.stream().reduce(0, (x, y) ->
                                    ((Double) (Math.sqrt(x) + Math.sqrt(y))).intValue()));
                        }
                    }
                }));
        threads.forEach(Thread::start);
        TimeUnit.MILLISECONDS.sleep(1000);
        isActive.set(false);
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
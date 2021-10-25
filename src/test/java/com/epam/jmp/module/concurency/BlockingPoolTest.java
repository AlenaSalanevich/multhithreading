package com.epam.jmp.module.concurency;

import com.epam.jmp.module.concurency.pool.BlockingObjectPool;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static org.testng.Assert.assertNotNull;


public class BlockingPoolTest {
    final AtomicBoolean isActive = new AtomicBoolean(true);

    @BeforeMethod
    public void setUp() {
    }


    @Test
    public void test() {
        BlockingObjectPool objectPool = new BlockingObjectPool(1);
        List<Thread> threadList = Arrays.asList(new Thread(() -> assertNotNull(objectPool.get())),
                new Thread(() ->
                        objectPool.take("Alena")));
        threadList.forEach(Thread::start);
        threadList.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                t.interrupt();
            }
        });
    }

    @Test
    public void testLoop() throws InterruptedException {
        BlockingObjectPool objectPool = new BlockingObjectPool(10);
        List<Thread> threadList = Arrays.asList(new Thread(() -> {
                    while (isActive.get()) {
                        assertNotNull(objectPool.get());
                    }
                }),
                new Thread(() -> {
                    while (isActive.get()) {
                        objectPool.take("Alena" + ThreadLocalRandom.current().nextInt(0, 1000));
                    }
                }
                ));
        threadList.forEach(Thread::start);
        TimeUnit.MILLISECONDS.sleep(30000);
        isActive.set(false);
        for (Thread t : threadList) {
            t.join();
        }
    }
}
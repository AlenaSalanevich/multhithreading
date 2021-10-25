package com.epam.jmp.module.concurency.pool;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pool that block when it has not any items or it full
 */
public class BlockingObjectPool {

    private final Queue<Object> pool;
    private final AtomicInteger counter;
    private final int size;

    /**
     * Creates filled pool of passed size
     *
     * @param size of pool
     */
    public BlockingObjectPool(int size) {
        this.pool = new PriorityQueue<>(size);
        this.counter = new AtomicInteger(0);
        this.size = size;
    }

    /**
     * Gets object from pool or blocks if pool is empty
     *
     * @return object from pool
     */
    public synchronized Object get() {
        while (counter.get() <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Object poll = pool.poll();
            counter.getAndDecrement();
            return poll;
        } catch (Exception e) {
            counter.getAndIncrement();
            throw new RuntimeException(e);
        } finally {
            notifyAll();
        }
    }

    /**
     * Puts object to pool or blocks if pool is full
     *
     * @param object to be taken back to pool
     */
    public synchronized void take(Object object) {
        while (counter.get() >= size) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            pool.add(object);
            counter.getAndIncrement();
        } catch (Exception e) {
            counter.getAndDecrement();
            throw new RuntimeException(e);
        } finally {
            notifyAll();
        }
    }
}

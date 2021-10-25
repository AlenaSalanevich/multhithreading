package com.epam.jmp.module.concurency.experiment;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ExperimentConcurrentMap<K, V> extends HashMap<K, V> implements ConcurrentMap<K, V> {

    private final Lock lock = new ReentrantLock();

    @Override
    public V putIfAbsent(K key, V value) {
        lock.lock();
        try {
            return super.putIfAbsent(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        lock.lock();
        try {
            return super.remove(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        lock.lock();
        try {
            return super.replace(key, oldValue, newValue);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        lock.lock();
        try {
            return super.replace(key, value);
        } finally {
            lock.unlock();
        }
    }
}
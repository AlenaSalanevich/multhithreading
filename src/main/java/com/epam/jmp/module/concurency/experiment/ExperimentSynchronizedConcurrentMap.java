package com.epam.jmp.module.concurency.experiment;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;


public class ExperimentSynchronizedConcurrentMap<K, V> extends HashMap<K, V> implements ConcurrentMap<K, V> {

    @Override
    public synchronized V putIfAbsent(K key, V value) {
        return super.putIfAbsent(key, value);
    }

    @Override
    public synchronized boolean remove(Object key, Object value) {
        return super.remove(key, value);
    }

    @Override
    public synchronized boolean replace(K key, V oldValue, V newValue) {
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public synchronized V replace(K key, V value) {
        return super.replace(key, value);
    }
}

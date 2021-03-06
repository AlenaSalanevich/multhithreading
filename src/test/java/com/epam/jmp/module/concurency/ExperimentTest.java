package com.epam.jmp.module.concurency;

import com.epam.jmp.module.concurency.experiment.Experiment;
import com.epam.jmp.module.concurency.experiment.ExperimentConcurrentMap;
import com.epam.jmp.module.concurency.experiment.ExperimentSynchronizedConcurrentMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.testng.Assert.*;


public class ExperimentTest {
    public static final int MAX = 2000;
    public static final int MIN = 0;
    Map<Integer, String> map = new HashMap<>();
    Experiment experiment = new Experiment();
    AtomicInteger successCount = new AtomicInteger(MIN);

    @BeforeMethod
    public void setUp() {
        experiment.setFailsCount(new AtomicInteger(0));
        successCount.set(0);
    }

    @Test
    public void testFixedPool() {
        IntStream.range(MIN, MAX).parallel().forEach(i ->
                Executors.newFixedThreadPool(2).execute(() -> {
                    experiment.modify(map);
                    successCount.getAndIncrement();
                }));
        assertTrue(successCount.get() < MAX);
        assertTrue(successCount.get() > MIN);
        assertTrue(experiment.getFailsCount().get() < MAX);
        assertTrue(experiment.getFailsCount().get() > MIN);
    }

    @Test
    public void test() {
        IntStream.range(MIN, MAX).parallel().forEach(i -> {
            experiment.modify(map);
            successCount.getAndIncrement();
        });
        assertEquals(successCount.get(), MAX);
        assertTrue(experiment.getFailsCount().get() > MIN);
        assertTrue(experiment.getFailsCount().get() < MAX);
    }

    @Test
    public void testConcurrentMap() {
        AtomicInteger successCount = new AtomicInteger(MIN);
        IntStream.range(MIN, MAX).parallel().forEach(i ->
                Executors.newSingleThreadExecutor().execute(() -> {
                    experiment.modify(new ConcurrentHashMap<>());
                    successCount.getAndIncrement();
                }));

        assertTrue(successCount.get() < MAX);
        assertTrue(successCount.get() > MIN);
        assertTrue(experiment.getFailsCount().get() == MIN);
    }

    @Test
    public void testCustomLockConcurrentMap() {
        AtomicInteger successCount = new AtomicInteger(MIN);
        IntStream.range(MIN, MAX).parallel().forEach(i ->
                Executors.newSingleThreadExecutor().execute(() -> {
                    experiment.modify(new ExperimentConcurrentMap<>());
                    successCount.getAndIncrement();
                }));

        assertTrue(successCount.get() < MAX);
        assertTrue(successCount.get() > MIN);
        assertTrue(experiment.getFailsCount().get() == MIN);
    }

    @Test
    public void testCustomSynchronizedConcurrentMap() {
        AtomicInteger successCount = new AtomicInteger(MIN);
        IntStream.range(MIN, MAX).parallel().forEach(i ->
                Executors.newSingleThreadExecutor().execute(() -> {
                    experiment.modify(new ExperimentSynchronizedConcurrentMap<>());
                    successCount.getAndIncrement();
                }));

        assertTrue(successCount.get() < MAX);
        assertTrue(successCount.get() > MIN);
        assertTrue(experiment.getFailsCount().get() == MIN);
    }
}
package com.example;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.producer.HelloTaskProducer;

@Component
public class ScheduledTask {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final HelloTaskProducer producer;

    public ScheduledTask(HelloTaskProducer producer) {
        this.producer = producer;
    }

    @Scheduled(fixedRate = 100)
    public void putTask() {
        producer.putTask(counter.incrementAndGet());
    }
}

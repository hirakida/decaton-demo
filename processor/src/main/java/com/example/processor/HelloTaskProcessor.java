package com.example.processor;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.protocol.Tasks.HelloTask;
import com.google.protobuf.Timestamp;

import com.linecorp.decaton.processor.DecatonProcessor;
import com.linecorp.decaton.processor.ProcessingContext;

@Component
public class HelloTaskProcessor implements DecatonProcessor<HelloTask> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloTaskProcessor.class);

    @Override
    public void process(ProcessingContext<HelloTask> context, HelloTask task) throws InterruptedException {
        final Timestamp timestamp = task.getCreatedAt();
        final Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        LOGGER.info("message={} timestamp={} metadata={}", task.getMessage(), instant, context.metadata());
        TimeUnit.MILLISECONDS.sleep(1000);
    }
}

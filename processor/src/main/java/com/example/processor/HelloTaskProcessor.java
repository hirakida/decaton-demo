package com.example.processor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

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
        Timestamp timestamp = task.getCreatedAt();
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(),
                                                                               timestamp.getNanos()),
                                                         TimeZone.getDefault().toZoneId());
        LOGGER.info("message={} createdAt={}", task.getMessage(), dateTime);
    }
}

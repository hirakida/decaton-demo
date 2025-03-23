package com.example.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.protocol.Tasks.HelloTask;
import com.google.protobuf.Timestamp;

import com.linecorp.decaton.client.DecatonClient;

@Component
public class HelloTaskProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloTaskProducer.class);
    private final DecatonClient<HelloTask> client;

    public HelloTaskProducer(DecatonClient<HelloTask> client) {
        this.client = client;
    }

    public void putTask(int key) {
        final long millis = System.currentTimeMillis();
        final Timestamp timestamp = Timestamp.newBuilder()
                                             .setSeconds(millis / 1000)
                                             .setNanos((int) ((millis % 1000) * 1000000))
                                             .build();
        final HelloTask task = HelloTask.newBuilder()
                                        .setMessage("Hello " + key)
                                        .setCreatedAt(timestamp)
                                        .build();

        client.put(String.valueOf(key), task)
              .whenComplete((result, e) -> {
                  LOGGER.info("id={}", result.id());
                  if (e != null) {
                      LOGGER.error("{}", e.getMessage(), e);
                  }
              });
    }
}

package com.example.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.protocol.Tasks.HelloTask;
import com.google.protobuf.MessageLite;

import com.linecorp.decaton.client.DecatonClient;
import com.linecorp.decaton.protobuf.ProtocolBuffersSerializer;

@Configuration
public class DecatonClientConfig {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String APPLICATION_ID = "decaton-demo";
    private static final String TOPIC = "topic1";

    @Bean
    public DecatonClient<HelloTask> helloClient() {
        return newClient(TOPIC);
    }

    private static <T extends MessageLite> DecatonClient<T> newClient(String topic) {
        Properties config = new Properties();
        config.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        return DecatonClient.producing(topic, new ProtocolBuffersSerializer<T>())
                            .applicationId(APPLICATION_ID)
                            .producerConfig(config)
                            .build();
    }
}

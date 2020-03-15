package com.example.processor;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.protocol.Tasks.HelloTask;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;

import com.linecorp.decaton.processor.DecatonProcessor;
import com.linecorp.decaton.processor.ProcessorProperties;
import com.linecorp.decaton.processor.ProcessorsBuilder;
import com.linecorp.decaton.processor.Property;
import com.linecorp.decaton.processor.PropertySupplier;
import com.linecorp.decaton.processor.StaticPropertySupplier;
import com.linecorp.decaton.processor.runtime.ProcessorSubscription;
import com.linecorp.decaton.processor.runtime.SubscriptionBuilder;
import com.linecorp.decaton.protobuf.ProtocolBuffersDeserializer;

@Configuration
public class ProcessorConfig {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String CLIENT_ID = "decaton-processor";
    private static final String GROUP_ID = "decaton-demo";

    @Bean
    public ProcessorSubscription processorSubscription() {
        return processorSubscription("hello-processor", "topic1",
                                     HelloTask.parser(), new HelloTaskProcessor());
    }

    private static <T extends GeneratedMessageV3> ProcessorSubscription processorSubscription(
            String subscriptionId, String topic, Parser<T> parser, DecatonProcessor<T> processor) {
        Properties consumerConfig = new Properties();
        consumerConfig.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        consumerConfig.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerConfig.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        ProcessorsBuilder<T> processorsBuilder =
                ProcessorsBuilder.consuming(topic, new ProtocolBuffersDeserializer<>(parser))
                                 .thenProcess(processor);

        PropertySupplier propertySupplier =
                StaticPropertySupplier.of(
                        Property.ofStatic(ProcessorProperties.CONFIG_PARTITION_CONCURRENCY, 10),
                        Property.ofStatic(ProcessorProperties.CONFIG_MAX_PENDING_RECORDS, 100));

        return SubscriptionBuilder.newBuilder(subscriptionId)
                                  .processorsBuilder(processorsBuilder)
                                  .consumerConfig(consumerConfig)
                                  .properties(propertySupplier)
                                  .buildAndStart();
    }
}

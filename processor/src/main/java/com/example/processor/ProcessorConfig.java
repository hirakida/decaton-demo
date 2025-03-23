package com.example.processor;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.protocol.Tasks.HelloTask;
import com.google.protobuf.GeneratedMessage;

import com.linecorp.decaton.processor.runtime.ProcessorProperties;
import com.linecorp.decaton.processor.runtime.ProcessorSubscription;
import com.linecorp.decaton.processor.runtime.ProcessorsBuilder;
import com.linecorp.decaton.processor.runtime.Property;
import com.linecorp.decaton.processor.runtime.PropertySupplier;
import com.linecorp.decaton.processor.runtime.StaticPropertySupplier;
import com.linecorp.decaton.processor.runtime.SubscriptionBuilder;
import com.linecorp.decaton.protobuf.ProtocolBuffersDeserializer;

@Configuration
public class ProcessorConfig {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "topic1";
    private static final String SUBSCRIPTION_ID = "hello-task-processor";
    private static final String GROUP_ID = "group1";

    @Bean
    public ProcessorSubscription helloProcessorSubscription(HelloTaskProcessor processor) {
        final ProcessorsBuilder<HelloTask> processorsBuilder =
                ProcessorsBuilder.consuming(TOPIC, new ProtocolBuffersDeserializer<>(HelloTask.parser()))
                                 .thenProcess(processor);
        return newProcessorSubscription(SUBSCRIPTION_ID, processorsBuilder);
    }

    private static <T extends GeneratedMessage> ProcessorSubscription newProcessorSubscription(
            String subscriptionId, ProcessorsBuilder<T> processorsBuilder) {
        final Properties config = new Properties();
        config.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        final PropertySupplier propertySupplier =
                StaticPropertySupplier.of(
                        Property.ofStatic(ProcessorProperties.CONFIG_PARTITION_CONCURRENCY, 5),
                        Property.ofStatic(ProcessorProperties.CONFIG_MAX_PENDING_RECORDS, 100));

        return SubscriptionBuilder.newBuilder(subscriptionId)
                                  .processorsBuilder(processorsBuilder)
                                  .consumerConfig(config)
                                  .properties(propertySupplier)
                                  .buildAndStart();
    }
}

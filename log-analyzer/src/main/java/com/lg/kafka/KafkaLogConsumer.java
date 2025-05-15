package com.lg.kafka;

import com.lg.loganalyzer.LogEntry;
import com.lg.loganalyzer.LogParser;
import com.lg.processor.LogProcessor;
import com.lg.report.LogAggregator;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class KafkaLogConsumer {


    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "log-analyzer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> listenerContainer(LogProcessor processor) {
        ContainerProperties containerProps = new ContainerProperties("log-events");
        containerProps.setMessageListener((MessageListener<String, String>) record -> {
            handleMessage(record, processor);
        });
        return new KafkaMessageListenerContainer<>(consumerFactory(), containerProps);
    }

    private void handleMessage(ConsumerRecord<String, String> record, LogProcessor processor) {
        System.out.println("Received: " + record.value());
        LogEntry entry = LogParser.parseLine(record.value());
        if (entry != null) {
            processor.process(entry);
        }
    }

    @Bean
    public LogProcessor logProcessor() {
        return new LogProcessor();
    }

}

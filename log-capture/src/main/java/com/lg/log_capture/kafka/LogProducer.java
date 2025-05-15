package com.lg.log_capture.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogProducer {

    @Value("${log.topic.name}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public LogProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLog(String logLine) {
        System.out.println("Trying to send to Kafka: " + logLine);
        System.out.println("Topic: " + topic);

        kafkaTemplate.send(topic, logLine).thenAccept(result -> {
            System.out.println("Successfully sent to Kafka: " + logLine);
        }).exceptionally(ex -> {
            System.err.println("Failed to send log to Kafka: " + ex);
            return null;
        });
    }

}

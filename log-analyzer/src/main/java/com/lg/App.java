package com.lg;

import com.lg.kafka.KafkaLogConsumer;
import com.lg.processor.LogProcessor;
import com.lg.report.LogAggregator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Timer;
import java.util.TimerTask;

public class App
{
    public static void main(String[] args) {
        System.out.println("Kafka Log Analyzer Started...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(KafkaLogConsumer.class);

        LogProcessor processor = context.getBean(LogProcessor.class);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LogAggregator aggregator = processor.getAggregator();

                System.out.println("\nReport:");
                System.out.println("Top 404 IPs: " + aggregator.top404IPs(10));
                System.out.println("Slowest Requests:");
                aggregator.slowestRequests(5).forEach(System.out::println);
                System.out.println("Status Counts: " + aggregator.statusCodeCounts());
            }
        }, 10000, 30000);
    }
}

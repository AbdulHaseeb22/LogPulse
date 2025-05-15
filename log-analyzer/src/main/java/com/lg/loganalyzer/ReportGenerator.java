package com.lg.loganalyzer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
public class ReportGenerator {
    public static void generateReport(List<LogEntry> entries) {
        System.out.println("\n===== Log Report =====");

        // 1. Top 10 IPs by total request count
        System.out.println("\nTop 10 IPs by Request Count:");
        Map<String, Long> topIPs = entries.stream()
                .collect(Collectors.groupingBy(LogEntry::getIpAddress, Collectors.counting()));

        topIPs.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.printf("%s: %d requests%n", e.getKey(), e.getValue()));

        // 2. Top 404-causing IPs
        System.out.println("\nTop IPs Causing 404 Errors:");
        Map<String, Long> error404IPs = entries.stream()
                .filter(e -> e.getStatusCode() == 404)
                .collect(Collectors.groupingBy(LogEntry::getIpAddress, Collectors.counting()));

        error404IPs.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.printf("%s: %d 404s%n", e.getKey(), e.getValue()));

        // 3. Requests per endpoint
        System.out.println("\nRequests per Endpoint:");
        Map<String, Long> endpointHits = entries.stream()
                .collect(Collectors.groupingBy(LogEntry::getEndpoint, Collectors.counting()));

        endpointHits.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.printf("%s: %d hits%n", e.getKey(), e.getValue()));

        System.out.println("\n========================");
    }
}

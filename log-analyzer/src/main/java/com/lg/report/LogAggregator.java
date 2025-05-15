package com.lg.report;


import com.lg.loganalyzer.LogEntry;

import java.util.*;
import java.util.stream.Collectors;

public class LogAggregator {
    private final List<LogEntry> entries = new ArrayList<>();

    public void add(LogEntry entry) {
        entries.add(entry);
    }

    public Map<String, Long> top404IPs(int limit) {
        return entries.stream()
                .filter(e -> e.getStatusCode() == 404)
                .collect(Collectors.groupingBy(LogEntry::getIpAddress, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public List<LogEntry> slowestRequests(int limit) {
        return entries.stream()
                .sorted(Comparator.comparingLong(LogEntry::getResponseTime).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<Integer, Long> statusCodeCounts() {
        return entries.stream()
                .collect(Collectors.groupingBy(LogEntry::getStatusCode, Collectors.counting()));
    }
}
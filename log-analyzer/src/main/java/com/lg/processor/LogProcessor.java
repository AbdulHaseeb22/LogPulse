package com.lg.processor;
import com.lg.loganalyzer.LogEntry;
import com.lg.report.LogAggregator;

public class LogProcessor {
    private final LogAggregator aggregator = new LogAggregator();

    public void process(LogEntry entry) {
        aggregator.add(entry);
        System.out.println("Parsed Entry: " + entry);
    }

    public LogAggregator getAggregator() {
        return aggregator;
    }
}


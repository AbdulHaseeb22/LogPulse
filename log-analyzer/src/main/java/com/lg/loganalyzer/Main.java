package com.lg.loganalyzer;

import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Path logFile = Path.of("logs/access.log");

        try{
            List<LogEntry> entries = LogParser.parse(logFile);

            System.out.println("Parsed Log Entries");
            entries.forEach(System.out::println);

            System.out.printf("Total entries parsed: %d%n", entries.size());

            ReportGenerator.generateReport(entries);


        }catch (Exception e){
            System.err.println("Failed to parse log File: "+ e.getMessage());
        }

    }
}

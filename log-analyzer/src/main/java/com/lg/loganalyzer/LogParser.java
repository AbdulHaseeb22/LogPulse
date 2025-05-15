package com.lg.loganalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogParser {

    private static final Pattern logPattern = Pattern.compile(
            // Example line: 127.0.0.1 - - [15/May/2025:14:23:15 +0000] "GET /home HTTP/1.1" 200 1043
            "^(\\S+) - - \\[(.*?)\\] \"(\\w+) (.*?) HTTP/1.1\" (\\d{3}) \\d+"
    );

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    public static List<LogEntry> parse(Path filePath) throws IOException {
        return Files.lines(filePath)
                .map(LogParser::parseLine)
                .filter(entry->entry != null)
                .collect(Collectors.toList());
    }

    public static LogEntry parseLine(String line) {
        try {
            Pattern logPattern = Pattern.compile(
                    "^(\\S+) - - \\[(.+?)\\] \"(\\S+) (\\S+) HTTP/\\d\\.\\d\" (\\d{3}) (\\d+)$"
            );
            Matcher matcher = logPattern.matcher(line);
            if (!matcher.matches()) return null;

            String ip = matcher.group(1);
            String rawTimestamp = matcher.group(2);
            String method = matcher.group(3);
            String uri = matcher.group(4);
            int status = Integer.parseInt(matcher.group(5));
            long responseTime = Long.parseLong(matcher.group(6));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            LocalDateTime timestamp = LocalDateTime.parse(rawTimestamp, formatter);

            System.out.printf("Parsed: ip=%s, status=%d, time=%dms%n", ip, status, responseTime);
            return new LogEntry(ip, timestamp, method, uri, status, responseTime);

        } catch (Exception e) {
            System.err.println("Failed to parse line: " + line + " → " + e.getMessage());
            return null;
        }
    }



}

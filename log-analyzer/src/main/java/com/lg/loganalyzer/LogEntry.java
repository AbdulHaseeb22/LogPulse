package com.lg.loganalyzer;

import java.time.LocalDateTime;

public class LogEntry {
    private String ipAddress;
    private LocalDateTime timestamp;
    private String method;
    private String endpoint;
    private int statusCode;

    private long responseTime;

    public LogEntry(String ipAddress, LocalDateTime timestamp, String method, String endpoint, int statusCode, long responseTime) {
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.method = method;
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMethod() {
        return method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getResponseTime(){
        return responseTime;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s %s -> %d (%dms)", timestamp, ipAddress, method, endpoint, statusCode, responseTime);
    }
}

package com.lg.log_capture.middleware;

import com.lg.log_capture.kafka.LogProducer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class RequestLogFilter extends OncePerRequestFilter {

    private final LogProducer logProducer;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public RequestLogFilter(LogProducer logProducer) {
        this.logProducer = logProducer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        System.out.println("🚦 Inside RequestLogFilter for: " + request.getRequestURI());
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();

            String ip = request.getRemoteAddr();
            String timestamp = ZonedDateTime.now().format(formatter);
            String method = request.getMethod();
            String uri = request.getRequestURI();
            int status = response.getStatus();
            long duration = end - start;

            String logLine = String.format(
                    "%s - - [%s] \"%s %s HTTP/1.1\" %d %d",
                    ip, timestamp, method, uri, status, duration
            );

            logProducer.sendLog(logLine);
        }
    }
}

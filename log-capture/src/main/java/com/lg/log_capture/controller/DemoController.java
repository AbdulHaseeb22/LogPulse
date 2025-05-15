package com.lg.log_capture.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/fail")
    public String fail() {
        throw new RuntimeException("Oops");
    }
}

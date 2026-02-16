package com.suprun.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        log.info("Starting DemoApplication...");
        SpringApplication.run(DemoApplication.class, args);
        log.info("DemoApplication started");
    }

}

package org.bilyoner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LiveBettingApp {
    public static void main(String[] args) {
        SpringApplication.run(LiveBettingApp.class, args);
    }
}
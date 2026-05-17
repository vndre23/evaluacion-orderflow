package com.hexagonal.mitocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hexagonal.mitocode")
public class OrderFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderFlowApplication.class, args);
    }
}

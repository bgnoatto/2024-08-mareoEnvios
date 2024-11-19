package com.entregable.sistema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.entregable.sistema")
public class SistemaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SistemaApplication.class, args);
    }
}
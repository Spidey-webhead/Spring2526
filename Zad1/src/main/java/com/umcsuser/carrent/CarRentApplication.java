package com.umcsuser.carrent;

import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarRentApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CarRentApplication.class);
        app.run(args);
    }
}
package com.sid.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MongoDBServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(MongoDBServiceApp.class, args);
    }
}

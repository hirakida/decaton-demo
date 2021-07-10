package com.example;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.producer.HelloTaskProducer;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final HelloTaskProducer producer;

    public Application(HelloTaskProducer producer) {
        this.producer = producer;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 100; i++) {
            producer.putTask(i);
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

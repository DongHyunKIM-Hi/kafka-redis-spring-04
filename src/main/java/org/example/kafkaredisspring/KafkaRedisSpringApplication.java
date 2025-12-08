package org.example.kafkaredisspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class KafkaRedisSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaRedisSpringApplication.class, args);
    }

}

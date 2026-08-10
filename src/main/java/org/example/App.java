package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableScheduling
@EnableKafka //This enables kafka listeners/consumers
@EnableCaching
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
        System.out.println("Application Sound and running!");
    }
}

package com.everis.projetobanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static KafkaStart.kafkaStart.serversStart;
import static KafkaStart.kafkaStart.zookeperStart;


@Configuration
@ComponentScan
@EnableAutoConfiguration

@SpringBootApplication
public class ProjetoBancoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoBancoApplication.class, args);
        zookeperStart();
        serversStart();

    }

}

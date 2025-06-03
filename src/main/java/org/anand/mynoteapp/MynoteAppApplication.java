package org.anand.mynoteapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MynoteAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MynoteAppApplication.class, args);
        System.out.println("Mynote-App-Application");
    }

}

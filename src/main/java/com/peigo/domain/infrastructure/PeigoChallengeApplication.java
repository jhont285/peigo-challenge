package com.peigo.domain.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class PeigoChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeigoChallengeApplication.class, args);
    }

}

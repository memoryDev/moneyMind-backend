package dev.momory.moneymindbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoneyMindBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyMindBackendApplication.class, args);
    }

}

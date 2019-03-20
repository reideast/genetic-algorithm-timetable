package net.andreweast;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // ModelMapper is used to map @Entities to DTOs, which are for front-facing consumption
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

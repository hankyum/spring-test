package com.hank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hank.mvc.validator.ClientValidationGenerator;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
	public static ClientValidationGenerator formValidatinConfig;
	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        formValidatinConfig = context.getBean(ClientValidationGenerator.class);
    }
}

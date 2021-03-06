package com.hank;

import com.hank.domain.ValidationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hank.web.validator.AbstractValidationAdapter;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
	private static ConfigurableApplicationContext context;
	public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
    }

	public static AbstractValidationAdapter getValidationAdapter(Class<? extends AbstractValidationAdapter> cls) {
		return context.getBean(cls);
	}

	public static ValidationConfig getValidationConfig() {
		return context.getBean(ValidationConfig.class);
	}
}

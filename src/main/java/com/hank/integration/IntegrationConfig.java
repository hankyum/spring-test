package com.hank.integration;

import java.io.File;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.MessageChannel;

import com.hank.batch.config.PersonBatchConfig;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@IntegrationComponentScan
@Import(PersonBatchConfig.class)
public class IntegrationConfig {
	@Bean
	@InboundChannelAdapter(poller=@Poller(fixedRate="1000"), value = "personDataChannel")
	public MessageSource<File> personDataMessageSource() {
		return new FileReadingMessageSource() {{
			setDirectory(new File("D:/workspace/spring-test"));
			setFilter(new SimplePatternFileListFilter("person-data*.csv"));
		}};
	}
	
	@Bean
	public MessageChannel personDataChannel() {
		return new QueueChannel(2);
	}
	
	@Bean
	public MessageChannel personStrChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel personObjChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel personQueryChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel splitPersonChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel sumAgeChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel routerChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel router1() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel router2() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel totalPersonAgeChannel() {
		return new DirectChannel();
	}
}

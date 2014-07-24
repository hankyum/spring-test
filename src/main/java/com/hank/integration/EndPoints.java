package com.hank.integration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;

@Configuration
@MessageEndpoint
public class EndPoints {
	private static Logger logger = Logger.getLogger(EndPoints.class);
	@Autowired
	@Qualifier("importUserJob")
	private Job importUserJob;
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private PersonRepository personRepository;
	private String fileParameterName = "fileName";

	@ServiceActivator(inputChannel = "personObjChannel", outputChannel = "routerChannel")
	public Person processPerson(Message<Person> payload) {
		personRepository.save(payload.getPayload());
		System.out.println(payload.getPayload().getFirstName());
		return payload.getPayload();
	}

	@ServiceActivator(inputChannel = "router1")
	public void router1(Message<Person> m) {
		System.out.println("Message from router 1 " + m.getPayload());
	}

	@ServiceActivator(inputChannel = "personDataChannel", requiresReply = "false", poller = @Poller(fixedRate = "500"))
	public void  personDataActivator(final Message<File> m)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException,
			IOException, InterruptedException {
		synchronized (m) {
			if (m.getPayload() == null)
				return;
			final String fileName = m.getPayload().getAbsolutePath();
			if (fileName == null)
				return;
			jobLauncher.run(importUserJob, new JobParametersBuilder(){{
				addString(fileParameterName, fileName);
			}}.toJobParameters());
		}
	}

	@ServiceActivator(inputChannel = "router2")
	public void router2(Message<Person> m) {
		System.out.println("Message from router 2 " + m.getPayload());
	}

	@ServiceActivator(inputChannel = "splitPersonChannel", outputChannel = "sumAgeChannel")
	public Message<Long> handleSplitPerson(Message<Person> p) {
		System.out.println("Message from splitPerson "
				+ p.getPayload().getAge());
		return MessageBuilder.withPayload(p.getPayload().getAge()).build();
	}

	@Aggregator(inputChannel = "sumAgeChannel")
	public void sumPersonAge(List<Message<Long>> ages) {
		long sum = 0;
		for (Message<Long> age : ages) {
			sum += age.getPayload();
		}
		System.out.println("Total Ages is : " + sum);
	}

	@SuppressWarnings("serial")
	@Splitter(inputChannel = "personQueryChannel", outputChannel = "splitPersonChannel")
	public List<Message<Person>> splitQueryPerson(final Message<String> m) {
		return new ArrayList<Message<Person>>() {
			{
				for (Person p : personRepository.findAll(new ArrayList<Long>() {
					{
						String[] ary = m.getPayload().split(",");
						for (String s : ary) {
							add(Long.valueOf(s));
						}
					}
				})) {
					add(MessageBuilder.withPayload(p).build());
				}
			}
		};
	}

	@Router(inputChannel = "routerChannel")
	public String router(Message<Person> m) {
		if (m.getPayload().getId() % 2 == 0) {
			return "router1";
		} else {
			return "rounter2";
		}
	}

	@Transformer(inputChannel = "personStrChannel", outputChannel = "personObjChannel")
	public Person transformPerson(String nameVal,
			@Header("spiltter") String spiltter) {
		String[] ary = nameVal.split(spiltter);
		return new Person(ary[0], ary[1]);
	}
}

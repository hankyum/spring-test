package com.hank.batch.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;

@Configuration
@EnableBatchProcessing
public class PersonBatchConfig {
	private static final Logger logger = Logger.getLogger(PersonBatchConfig.class);
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private ItemProcessor<Person, Person> processor;
	
	@Bean
	@StepScope
	/**
	 * Return type must be ItemStreamReader because the open method must exists after be proxyed
	 * in step scope
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public ItemStreamReader<Person> reader(@Value("#{jobParameters[fileName]}") String fileName) throws FileNotFoundException {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
		reader.setResource(new InputStreamResource(new FileInputStream(fileName)));
		reader.setLineMapper(new DefaultLineMapper<Person>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {"id", "firstName", "lastName", "age"});
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
					{
						setTargetType(Person.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemWriter<Person> writer() {
		return new ItemWriter<Person>() {
			@Override
			public void write(List<? extends Person> items) throws Exception {
				personRepository.save(items);
			}

		};
	}

	@Bean(name="importUserJob")
	public Job importUserJob(Step personStep) {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer())
				.flow(personStep).next(backupFileStep()).end().build();
	}
	
	@Bean
	@StepScope
	public Tasklet tasklet(@Value("#{jobParameters[fileName]}") final String fileName) {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution,
					ChunkContext chunkContext) throws Exception {
				if (fileName != null) {
					File dataFile = new File(fileName);
					File destDir = new File(dataFile.getParent() + File.separator + "personDataBack");
					try {
						FileUtils.moveToDirectory(dataFile, destDir, true);
						logger.info(String.format("Backup person data file %s to %s after prcessing", fileName, destDir.getAbsoluteFile()));;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		};
	}

	@Bean
	public Step backupFileStep() {
		return stepBuilderFactory.get("backupFileStep").tasklet(tasklet(null)).build();
	}

	@Bean
	public Step personStep() throws FileNotFoundException {
		return stepBuilderFactory.get("personStep").<Person, Person> chunk(10)
				.reader(reader(null)).processor(processor).writer(writer()).build();
	}

}

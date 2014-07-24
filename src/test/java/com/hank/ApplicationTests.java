package com.hank;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;
import com.hank.integration.PersonGateway;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {
	@Autowired
	PersonGateway personGateway;
	
	@Autowired
	PersonRepository personRepository;
	
	private Person testObj;
	
	@Before
	public void setUp() {
		testObj = new Person("sssss", "xfdfsf");
	}
	
	@Test
	public void testPersonGateway() throws InterruptedException {
		personGateway.createPerson(testObj);
		Thread.sleep(2000);
		Assert.assertTrue(personRepository.findAll().toString().contains(testObj.toString()));
	}

	@Test
	public void testPersonGatewayWithTransformer() throws InterruptedException {
		personGateway.createPerson(testObj.getFirstName().concat("|").concat(testObj.getLastName()));
		Iterable<Person> all = personRepository.findAll();
		System.out.println(all);
		Assert.assertTrue(all.toString().contains(testObj.toString()));
	}
	
	@Test
	public void testQueryPersonGateway() throws InterruptedException {
		personGateway.queryPerson("1,2");
	}
	
	@Test
	public void testIntegrationBatch() throws Exception {
		String dataDir = "D:/workspace/spring-test/";
		String backDir = dataDir + "personDataBack/";
		String file = "person-data.csv";
		File backFile = new File(backDir + file);
		File dataFile = new File(dataDir + file);
		FileUtils.moveFileToDirectory(backFile, new File(dataDir), false);
		Assert.assertFalse(backFile.exists());
		Assert.assertTrue(dataFile.exists());
		Thread.sleep(5000);
		Assert.assertTrue(backFile.exists());
		Assert.assertFalse(dataFile.exists());
		
	}
}

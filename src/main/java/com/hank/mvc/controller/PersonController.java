package com.hank.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;

@RestController
@RequestMapping(value = "/persons", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@RequestMapping
	public Iterable<Person> Persons() {
		return personRepository.findAll(); 
	}

	@RequestMapping(value = "/{id}")
	public Person Person(@PathVariable("id") Long id) {
		return personRepository.findOne(id);
	}

	public PersonRepository getPersonRepository() {
		return personRepository;
	}

	public void setPersonRepository(PersonRepository PersonRepository) {
		this.personRepository = PersonRepository;
	}

}

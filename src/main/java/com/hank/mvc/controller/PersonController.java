package com.hank.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@RequestMapping
	public Iterable<Person> persons() {
		return personRepository.findAll(); 
	}
	
	@RequestMapping(value = "/{id}")
	public ModelAndView Person(@PathVariable("id") Long id, Model model) {
		model.addAttribute("person", personRepository.findOne(id));
		return new ModelAndView("/personView");
	}

	public PersonRepository getPersonRepository() {
		return personRepository;
	}

	public void setPersonRepository(PersonRepository PersonRepository) {
		this.personRepository = PersonRepository;
	}

}

package com.hank.mvc.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;

@RestController
public class PController {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/")
	public ModelAndView index() {
		return new ModelAndView("/index");
	}

	@RequestMapping(value = "/msg")
	public String getMessage() {
		return messageSource.getMessage("age.validation", null, Locale.US);
	}

	@RequestMapping(value = "/person", method = RequestMethod.GET)
	public ModelAndView person(Person person, Model model) {
		return new ModelAndView("/person");
	}

	@RequestMapping(value = "/person", method = RequestMethod.POST)
	public ModelAndView person(Model model, @Valid Person person, BindingResult errors) {
		ModelAndView view = new ModelAndView("/person");
		if (errors.hasErrors()) {
			return view;
		}
		personRepository.save(person);
		model.addAttribute("id", person.getId());
		return new ModelAndView("personView");
	}

	@RequestMapping(value = "/person1", method = RequestMethod.GET)
	public ModelAndView person1(Person person, Model model) {
		return new ModelAndView("/person1");
	}

	@RequestMapping(value = "/person1", method = RequestMethod.POST)
	public Object person1(Model model, @Valid Person person, BindingResult errors) {
		ModelAndView view = new ModelAndView("/person1");
		if (errors.hasErrors()) {
			return view;
		}
		personRepository.save(person);
		model.addAttribute("id", person.getId());
		return person;
	}

	@RequestMapping("personLists")
	public ModelAndView personLists() {
		return new ModelAndView("personLists");
	}
}

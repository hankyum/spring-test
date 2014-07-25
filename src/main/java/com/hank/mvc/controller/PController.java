package com.hank.mvc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping(value = "/person", method=RequestMethod.POST)
	public ModelAndView person(Model model, @Valid Person person, BindingResult errors) {
		ModelAndView view = new ModelAndView("/person");
		if (errors.hasErrors()) {
			return view;
		}
		personRepository.save(person);
		model.addAttribute("id", person.getId());
		return new ModelAndView("personView");
	}
	
	@RequestMapping(value = "/person", method=RequestMethod.GET)
	public ModelAndView person(Person person, Model model) {
		model.addAttribute("test", " val");
		return new ModelAndView("/person");
	}
	
	@RequestMapping(value = "/personLists", method=RequestMethod.GET)
	public ModelAndView person() {
		return new ModelAndView("/personLists");
	}
}

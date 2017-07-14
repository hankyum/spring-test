package com.hank.web.controller;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;
import com.hank.domain.ValidationConfig;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Locale;

@RestController
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("/index");
    }

    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String getMessage() {
        return messageSource.getMessage("age.validation", null, Locale.US);
    }

    @RequestMapping(value = "/person1", method = RequestMethod.GET)
    public ModelAndView person(Person person, ValidationConfig config) {
        return new ModelAndView("/person-booststrap-validator");
    }

    @RequestMapping(value = "/person", method = RequestMethod.POST)
    public ModelAndView person(Model model, @Valid Person person, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("/person-booststrap-validator");
        }
        personRepository.save(person);
        model.addAttribute("id", person.getId());
        return new ModelAndView("personView");
    }

    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public ModelAndView person1(Person person, ValidationConfig config) {
        return new ModelAndView("/person-own-validator");
    }

    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "person", value = "用户详细实体 person", required = true, dataType = "Person")
    })
    @RequestMapping(value = "/person1", method = RequestMethod.POST)
    public Object person1(Model model, @Valid @RequestBody Person person, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("/person-own-validator");
        }
        personRepository.save(person);
        model.addAttribute("id", person.getId());
        return person;
    }
}

package com.hank.web.controller;

import com.hank.dao.PersonRepository;
import com.hank.domain.Person;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/persons")
public class PersonsController {

    @Autowired
    private PersonRepository personRepository;

    @ApiOperation("获取用户列表")
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Person> persons() {
        return personRepository.findAll();
    }

    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType="path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView Person(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", personRepository.findOne(id));
        return new ModelAndView("/personView");
    }
}

package com.hank.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hank.domain.Person;
 
@Repository("personRepository")
public interface PersonRepository extends CrudRepository<Person, Long> {
	
}

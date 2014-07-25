package com.hank.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.hank.domain.Person;

@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        person.setFirstName(firstName);
        person.setLastName(lastName);
        
        System.out.println("------------------Converting (" + person + ") into (" + person + ")");

        return person;
    }

}

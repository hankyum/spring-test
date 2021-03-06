package com.hank.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Person extends ValidationConfig {
	@Id
	@GeneratedValue
	private long id;
	@Column
	@Size(min=2, max=20)
	@NotNull
	private String firstName;
	@Column
	@Size(min=2, max=20)
	@NotNull
	private String lastName;
	@Column
	@Digits(message="age.validation", fraction = 0, integer = 200)
	@NotNull
	private Long age;

	public Person() {

	}

	public Person(String firstName2, String lastName2) {
		this.firstName = firstName2;
		this.lastName = lastName2;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "firstName: " + firstName + ", lastName: " + lastName;
	}
}

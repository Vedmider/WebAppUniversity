package com.study.projects.domain;

import java.util.Objects;

public abstract class Person {
	private String firstName;
	private String lastName;
	private int id;
	
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getFullName() {
		return  firstName + " " + lastName;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		
		Person person = (Person) obj;
		int idToCompare = person.getId();
		return person.getFullName().equals(getFullName()) && id == idToCompare;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
	}
}

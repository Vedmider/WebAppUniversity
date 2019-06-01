package com.study.projects.domain;

public class Student extends Person {
	private Group group;

	public Student(String firstName, String lastName) {
		super(firstName, lastName);
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}

package com.study.projects.domain;

import java.util.Objects;

public class Classroom {
	private int number;
	private int id;
	
	public Classroom(int number) {
		this.number = number;
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
		
		Classroom classroom = (Classroom) obj;
		return id == classroom.getId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
	public int getNumber() {
		return number;
	}

}

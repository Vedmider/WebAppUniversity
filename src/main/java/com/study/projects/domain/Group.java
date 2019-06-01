package com.study.projects.domain;

import java.util.Objects;

public class Group {
	private String name;
	private int id;
	
	public Group(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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
		
		Group group = (Group) obj;
		
		if (group.getId() == 0) {
			return false;
		}
		return id == group.getId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

}

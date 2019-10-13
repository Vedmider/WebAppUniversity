package com.study.projects.domain;

import java.time.LocalDateTime;

public class Lecture {
	private String subject;
	private Classroom classroom;
	private Group group;
	private Teacher teacher;
	private LocalDateTime date;
	private int id;
	
	public Lecture (String subject, Classroom classroom, Group group, Teacher teacher, LocalDateTime date) {
		this.subject = subject;
		this.classroom = classroom;
		this.group = group;
		this.teacher = teacher;
		this.date = date;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public Classroom getClassroom() {
		return classroom;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
}

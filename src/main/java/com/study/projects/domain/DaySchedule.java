package com.study.projects.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DaySchedule {
	private ArrayList <Lecture> lectures = new ArrayList<Lecture>();
	private LocalDate date;
	
	public DaySchedule(LocalDate date) {
		this.date = date;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public ArrayList<Lecture> getLectures() {
		return lectures;
	}
	
	public boolean addLecture(Lecture lecture) {
		LocalDateTime lectureDate = lecture.getDate();
		Teacher teacher = lecture.getTeacher();
		Group group = lecture.getGroup();
		Classroom classroom = lecture.getClassroom();
		
		if(!date.isEqual(lectureDate.toLocalDate())) {
			return false;
		}	
			
		for(Lecture l : lectures ) {
			if(lectureDate.isEqual(l.getDate())) {
				if(teacher.equals(l.getTeacher()) || group.equals(l.getGroup()) || classroom.equals(l.getClassroom())) {
					return false;
				}
			}	
		}	
		lectures.add(lecture);
		return true;
	}
	
	public ArrayList<Lecture> getTeacherDaySchedule(Teacher teacher){
		ArrayList<Lecture> tempSchedule = new ArrayList<>();
		
		for(Lecture l : lectures) {
			Teacher t = l.getTeacher();
			if(t.equals(teacher)) {
				tempSchedule.add(l);
			}
		}
		
		return tempSchedule;
	}
	
	public ArrayList<Lecture> getStudentDaySchedule(Student student){
		ArrayList<Lecture> tempSchedule = new ArrayList<>();
		
		for(Lecture l : lectures) {
			Group g = l.getGroup();
			if(g.equals(student.getGroup())) {
				tempSchedule.add(l);
			}
		}
		
		return tempSchedule;
	}
}

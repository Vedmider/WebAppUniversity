package com.study.projects.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DayScheduleTest {
	private DaySchedule testSchedule;
	private Teacher teacher;
	private Student student;
	private Group groupMTZ;

	@BeforeEach
	public void setUp() {
		teacher = new Teacher("Anton", "Chernushenko");
		groupMTZ = new Group("MTZ");
		student = new Student("Ioan", "Nazareta");
		student.setGroup(groupMTZ );
		Lecture lecture1 = new Lecture("Math", new Classroom(405), groupMTZ , teacher, LocalDateTime.of(2017, 6, 20, 11, 30)); 
		Lecture lecture2 = new Lecture("Geography", new Classroom(405), groupMTZ, teacher, LocalDateTime.of(2017, 6, 20, 12, 30));
		Lecture lecture3 = new Lecture("Singing", new Classroom(405), groupMTZ , teacher, LocalDateTime.of(2017, 6, 20, 13, 30));
		testSchedule = new DaySchedule(LocalDate.of(2017, 6, 20));
		testSchedule.addLecture(lecture1);
		testSchedule.addLecture(lecture2);
		testSchedule.addLecture(lecture3);
	}
	
	@Test
	public void testGetStudentScheduleWithCorrectStudent() {
		Group expectedGroup = student.getGroup();
		ArrayList<Lecture> studentSchedule = testSchedule.getStudentDaySchedule(student);
		
		for(Lecture l : studentSchedule) {
			assertEquals(expectedGroup, l.getGroup());
		}
	}
	
	@Test
	public void testGetStudentScheduleWithNotInADayScheduleStudentGroup() {
		Student wrongStudent = new Student("Jacob", "Pullman");
		Group group = new Group("SpeedFire");
		group.setId(352341413);
		wrongStudent.setGroup(group);
		ArrayList<Lecture> studentSchedule = testSchedule.getStudentDaySchedule(wrongStudent);
		assertTrue(studentSchedule.isEmpty());
	}
	
	@Test
	public void testGetTeacherScheduleWithCorrectTeacher() {
		ArrayList<Lecture> teacherSchedule = testSchedule.getTeacherDaySchedule(teacher);
		
		for(Lecture l : teacherSchedule) {
			assertEquals(teacher, l.getTeacher());
		}
	}
	
	@Test
	public void testGetTeacherScheduleWitNotInADayScheduleTeacher() {
		Teacher wrongTeacher = new Teacher("Oleg", "Lyashko");
		ArrayList<Lecture> teacherSchedule = testSchedule.getTeacherDaySchedule(wrongTeacher);
		
		assertTrue(teacherSchedule.isEmpty());
	}
	
	@Test
	public void testAddLectureWithNotMatchingDate() {
		Lecture anotherDayLecture = new Lecture("Math", new Classroom(405), groupMTZ , teacher, LocalDateTime.of(2017, 8, 12, 11, 30));
		assertFalse(testSchedule.addLecture(anotherDayLecture));
	}
	
	@Test
	public void testAddLectureWithOccupiedTeacher() {
		Lecture occupiedTeacherLecture = new Lecture("Math", new Classroom(340), new Group("BBS"), teacher, LocalDateTime.of(2017, 6, 20, 11, 30)); 
		assertFalse(testSchedule.addLecture(occupiedTeacherLecture));
	
	}
	
	@Test
	public void testAddLectureWithOccupiedGroup() {
		Lecture occupiedGroupLecture = new Lecture("Math", new Classroom(405), groupMTZ, new Teacher("Vlad", "Dracula"), LocalDateTime.of(2017, 6, 20, 11, 30));
		assertFalse(testSchedule.addLecture(occupiedGroupLecture));
	}
	
	@Test
	public void testAddLectureWithVacantGroupAndTeacher() {
		Lecture vacantTeacherGroupLecture = new Lecture("Math", new Classroom(405), groupMTZ , teacher, LocalDateTime.of(2017, 6, 20, 14, 30));
		assertTrue(testSchedule.addLecture(vacantTeacherGroupLecture));
	}
}

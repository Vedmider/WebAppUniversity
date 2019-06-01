package com.study.projects.domain;

import java.util.ArrayList;
import java.util.HashMap;

import com.study.projects.dao.ClassroomDao;
import com.study.projects.dao.GroupDao;
import com.study.projects.dao.LectureDao;
import com.study.projects.dao.StudentDao;
import com.study.projects.dao.TeacherDao;
import com.study.projects.dao.UniversityDBAccessException;

import java.time.LocalDate;

public class University {
	private ArrayList<Student> students = new ArrayList<>();
	private ArrayList<Teacher> teachers = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	private ArrayList<Classroom> classrooms = new ArrayList<>();
	private HashMap<LocalDate, DaySchedule> yearSchedule = new HashMap<LocalDate, DaySchedule>();
	private int universityId;
	private String universityName;
	private ClassroomDao classroomDao;
	private StudentDao studentDao;
	private TeacherDao teacherDao;
	private GroupDao groupDao;
	private LectureDao lectureDao;
	
	public University(String universityName, int universityId) {
		this.universityName = universityName;
		this.universityId = universityId;
		initiateDao(universityId);
		try{
			students = studentDao.getAll();
			teachers = teacherDao.getAll();
			groups = groupDao.getAll();
			classrooms = classroomDao.getAll();
			yearSchedule = makeYearShedule(lectureDao.getAll());
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
	
	}
	
	public University(String universityName, int universityId, ClassroomDao classroomDao, StudentDao studentDao,
						TeacherDao teacherDao, GroupDao groupDao, LectureDao lectureDao) {
		this.universityName = universityName;
		this.universityId = universityId;
		this.classroomDao = classroomDao;
		this.groupDao = groupDao;
		this.lectureDao = lectureDao;
		this.studentDao = studentDao;
		this.teacherDao = teacherDao;
		try {
			students = studentDao.getAll();
			teachers = teacherDao.getAll();
			groups = groupDao.getAll();
			classrooms = classroomDao.getAll();
			yearSchedule = makeYearShedule(lectureDao.getAll());
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
	
	}
	
	public int getId() {
		return universityId;
	}
	
	public String getName() {
		return universityName;
	}
	
	public boolean addStudent(Student student) {
		if (groups.contains(student.getGroup())) {
			return students.add(student);
		}
		return false;
	}
	
	public boolean removeStudent(Student student){
		return students.remove(student);  
	}
	
	public boolean addTeacher(Teacher teacher) {

			return teachers.add(teacher);
	}	
	
	public boolean removeTeacher(Teacher teacher) {
		return teachers.remove(teacher); 
	}
	
	public boolean addGroup(Group group) {
		
			return groups.add(group);
	}

	public boolean removeGroup(Group group) {
		return groups.remove(group);
	}
	
	public boolean addClassroom(Classroom classroom) {
		
			return classrooms.add(classroom);
	}

	public boolean removeClassroom(Classroom classroom) {
		return classrooms.remove(classroom);
	}
	
	public boolean addLecture(Lecture lecture) {
		if(!groups.contains(lecture.getGroup()) || !teachers.contains(lecture.getTeacher()) || !classrooms.contains(lecture.getClassroom())) {
			return false;
		}
		
		DaySchedule tempSchedule;
		LocalDate date = lecture.getDate().toLocalDate();
		
		if(yearSchedule.containsKey(date)) {
			tempSchedule = yearSchedule.get(date);    
		} else {
			tempSchedule = new DaySchedule(date);	
		}
	
		if(!tempSchedule.addLecture(lecture)) {
			return false;
		}
		
		
		tempSchedule.addLecture(lecture);
		yearSchedule.put(date, tempSchedule);
		return true;
	}
	
	public boolean removeLecture(Lecture lecture) {
		if(!groups.contains(lecture.getGroup()) || !teachers.contains(lecture.getTeacher()) || !classrooms.contains(lecture.getClassroom())) {
			return false;
		}
		
		DaySchedule tempSchedule;
		LocalDate date = lecture.getDate().toLocalDate();
		
		if (yearSchedule.containsKey(date)) {
			tempSchedule = yearSchedule.get(date);
			
			if(tempSchedule.getLectures().contains(lecture)) {
				return tempSchedule.getLectures().remove(lecture);
			}
		}
		return false;
	}
	
	public ArrayList<Student> getStudents() {
		return students;
	}
	
	public ArrayList<Teacher> getTeachers() {
		return teachers;
	}
	
	public ArrayList<Group> getGroups() {
		return groups;
	}
	
	public ArrayList<Classroom> getClassrooms() {
		return classrooms;
	} 
	
	public HashMap<LocalDate, DaySchedule> getYearSchedule() {
		return yearSchedule;
	}
	
	
	public ArrayList<Lecture> getTeacherMonthSchedule(int month, Teacher teacher) {
		if(!teachers.contains(teacher)) {
			return null;
		}
		DaySchedule tempDaySchedule;
		ArrayList<Lecture> tempLectureArray = new ArrayList<Lecture>();
		
		for(LocalDate date : yearSchedule.keySet()) {
			if(date.getMonthValue() == month) {
				tempDaySchedule = yearSchedule.get(date);
				tempLectureArray.addAll(tempDaySchedule.getTeacherDaySchedule(teacher));	
			}
		}
		return tempLectureArray;	
	} 
	
	public ArrayList<Lecture> getTeacherDaySchedule(LocalDate date, Teacher teacher) {
		if(!teachers.contains(teacher)) {
			return null;
		}
		
		DaySchedule tempDaySchedule = yearSchedule.get(date);
		return tempDaySchedule.getTeacherDaySchedule(teacher);
	}
	
	public ArrayList<Lecture> getStudentMonthSchedule(int month, Student student) {
		if(!students.contains(student)) {
			return null;
		}
		
		DaySchedule tempDaySchedule;
		ArrayList<Lecture> tempLectureArray = new ArrayList<Lecture>();
		
		for(LocalDate date : yearSchedule.keySet()) {
			if(date.getMonthValue() == month) {
				tempDaySchedule = yearSchedule.get(date);
				tempLectureArray.addAll(tempDaySchedule.getStudentDaySchedule(student));
			}
		}
		return tempLectureArray;
	}
	
	public ArrayList<Lecture> getStudentDaySchedule(LocalDate date, Student student) {
		if(!students.contains(student)) {
			return null;
		}
		
		DaySchedule tempDaySchedule = yearSchedule.get(date);
		return tempDaySchedule.getStudentDaySchedule(student);
	}
	
	
	private HashMap<LocalDate, DaySchedule> makeYearShedule(ArrayList<Lecture> lectures){
		HashMap<LocalDate, DaySchedule> tempYearSchedule = new HashMap<LocalDate, DaySchedule>();
		DaySchedule day;
		if(lectures == null) {
			System.out.println("lectures is null");
		}
		
		for(Lecture l : lectures) {
			if(tempYearSchedule.containsKey(l.getDate().toLocalDate())) {
				day = tempYearSchedule.get(l.getDate().toLocalDate());
				day.addLecture(l);
				tempYearSchedule.put(l.getDate().toLocalDate(), day);
			} else {
				day = new DaySchedule(l.getDate().toLocalDate());
				day.addLecture(l);
				tempYearSchedule.put(l.getDate().toLocalDate(), day);
			}
		}
		return tempYearSchedule;
	}
	
	private void initiateDao(int universityId) {
		classroomDao = new ClassroomDao(universityId);
		studentDao = new StudentDao(universityId);
		teacherDao = new TeacherDao(universityId);
		groupDao = new GroupDao(universityId);
		lectureDao = new LectureDao(universityId);
	}
	
	public StudentDao getStudentDao() {
		return studentDao;
	}
	
	public TeacherDao getTeacherDao() {
		return teacherDao;
	}
	
	public GroupDao getGroupDao() {
		return groupDao;
	}
	
	public ClassroomDao getClassroomDao() {
		return classroomDao;
	}
	
	public LectureDao getLectureDao() {
		return lectureDao;
	}
}

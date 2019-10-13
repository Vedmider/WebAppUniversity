package com.study.projects.domain;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.study.projects.dao.ClassroomDao;
import com.study.projects.dao.GroupDao;
import com.study.projects.dao.LectureDao;
import com.study.projects.dao.StudentDao;
import com.study.projects.dao.TeacherDao;
import com.study.projects.dao.UniversityDBAccessException;

import java.time.LocalDate;

public class University {
	private static final Logger logger = LoggerFactory.getLogger(University.class);
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
			logger.error("Can not create University object", e);
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
			logger.error("Can not create University object", e);
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
		Integer id = null;
		if(!groups.contains(lecture.getGroup()) || !teachers.contains(lecture.getTeacher()) || !classrooms.contains(lecture.getClassroom())) {
			return false;
		}
		
		logger.debug("adding lecture {} to University {}", lecture.getId(), this.getName());
		
		try {
			id = lectureDao.insertInToDB(lecture);
			if (id == null) {
				return false;
			} 
		} catch(UniversityDBAccessException e) {
			logger.error("Can not insert lecture to DB", e);
		}
		
		DaySchedule tempSchedule;
		LocalDate date = lecture.getDate().toLocalDate();
		lecture.setId(id);
		
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
		
		logger.debug("removing lecture {} to University {}", lecture.getId(), this.getName());
		try {
			lectureDao.removeFromDB(lecture);
		} catch(UniversityDBAccessException e) {
			logger.error("Can not remove from DB", e);
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
		
		logger.debug("getting teacher {} schedule for {} month to University {}", teacher.getFullName(), month, this.getName());
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
		
		logger.debug("getting teacher {} schedule for {} date to University {}", teacher.getFullName(), date.toString(), this.getName());
		DaySchedule tempDaySchedule = yearSchedule.get(date);
		return tempDaySchedule.getTeacherDaySchedule(teacher);
	}
	
	public ArrayList<Lecture> getStudentMonthSchedule(int month, Student student) {
		if(!students.contains(student)) {
			return null;
		}
		
		logger.debug("getting student {} schedule for {} month to University {}", student.getFullName(), month, this.getName());
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
		
		logger.debug("getting student {} schedule for {} month to University {}", student.getFullName(), date.toString(), this.getName());
		DaySchedule tempDaySchedule = yearSchedule.get(date);
		return tempDaySchedule.getStudentDaySchedule(student);
	}
	
	
	private HashMap<LocalDate, DaySchedule> makeYearShedule(ArrayList<Lecture> lectures){
		HashMap<LocalDate, DaySchedule> tempYearSchedule = new HashMap<LocalDate, DaySchedule>();
		DaySchedule day;
		if(lectures == null) {
			logger.info("there is no lectures to makeYearShedule in University {}", this.getName());
		}
		
		logger.debug("making year schedule for University {}", this.getName());
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
		logger.debug("initiating DAO classes for University {} ID {}", this.getName(), this.getId());
		classroomDao = new ClassroomDao(universityId);
		studentDao = new StudentDao(universityId);
		teacherDao = new TeacherDao(universityId);
		groupDao = new GroupDao(universityId);
		lectureDao = new LectureDao(universityId);
	}
	
	protected StudentDao getStudentDao() {
		return studentDao;
	}
	
	protected TeacherDao getTeacherDao() {
		return teacherDao;
	}
	
	protected GroupDao getGroupDao() {
		return groupDao;
	}
	
	protected ClassroomDao getClassroomDao() {
		return classroomDao;
	}
	
	protected LectureDao getLectureDao() {
		return lectureDao;
	}

	public Group getGroupById(Integer id){
		Group group = null;
		try{
			group = groupDao.getByPK(id);
		} catch (UniversityDBAccessException e ){
			logger.error("cannot get Group with ID = " + id, e);
		}
		return group;
	}

	public Student getStudentById (Integer id) {
		Student student = null;
		try{
			student = studentDao.getByPK(id);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not get Student with ID " + id, e);
		}
		return student;
	}

	public Teacher getTeacherById (Integer id) {
		Teacher teacher = null;
		try{
			teacher = teacherDao.getByPK(id);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not get teacher by id " + id, e);
		}
		return teacher;
	}

	public Classroom getClassroomById(Integer id) {
		Classroom classroom = null;
		try{
			classroom = classroomDao.getByPK(id);
		} catch (UniversityDBAccessException e) {
			logger.error("can not get classroom with such ID " + id, e);
		}

		return classroom;
	}

}

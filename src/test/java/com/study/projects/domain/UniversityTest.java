package com.study.projects.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.study.projects.dao.ClassroomDao;
import com.study.projects.dao.GroupDao;
import com.study.projects.dao.LectureDao;
import com.study.projects.dao.StudentDao;
import com.study.projects.dao.TeacherDao;
import com.study.projects.dao.UniversityDBAccessException;

public class UniversityTest {
	private University university;
	private ClassroomDao classroomDao = mock(ClassroomDao.class);
	private GroupDao groupDao = mock(GroupDao.class);
	private TeacherDao teacherDao = mock(TeacherDao.class);
	private StudentDao studentDao = mock(StudentDao.class);
	private LectureDao lectureDao = mock(LectureDao.class);
	private Teacher teacherChernushenko;
	private Teacher teacherLane;
	private Student studentJobs;
	private Group groupMTZ;
	private Group groupBBS;
	
	
	
	@BeforeEach
	public void setUp() throws UniversityDBAccessException {
		ArrayList<Classroom> universityClassrooms = new ArrayList<Classroom>();
		Classroom class405 = new Classroom(405);
		Classroom class407 = new Classroom(407);
		Classroom class465 = new Classroom(465);
		Classroom class401 = new Classroom(401);
		class405.setId(126);
		class407.setId(127);
		class465.setId(128);
		class401.setId(129);
		universityClassrooms.add(class405);
		universityClassrooms.add(class401);
		universityClassrooms.add(class465);
		universityClassrooms.add(class401);
		when(classroomDao.getAll()).thenReturn(universityClassrooms);
		
		ArrayList<Group> universityGroups = new ArrayList<Group>();
		groupMTZ = new Group("MTZ");
		groupMTZ.setId(311);
		groupBBS = new Group("BBS");
		groupBBS.setId(312);
		universityGroups.add(groupMTZ);
		universityGroups.add(groupBBS);
		when(groupDao.getAll()).thenReturn(universityGroups);
		
		ArrayList<Teacher> universityTeachers = new ArrayList<Teacher>(); 
		teacherChernushenko = new Teacher("Anton", "Chernushenko");
		teacherChernushenko.setId(160);
		teacherLane = new Teacher("Lois", "Lane");
		teacherLane.setId(2);
		universityTeachers.add(teacherChernushenko);
		universityTeachers.add(teacherLane);
		when(teacherDao.getAll()).thenReturn(universityTeachers);
		
		ArrayList<Student> universityStudents = new ArrayList<Student>();
		studentJobs = new Student ("Steve", "Jobs");
		studentJobs.setGroup(groupMTZ);
		studentJobs.setId(3);
		universityStudents.add(studentJobs);
		when(studentDao.getAll()).thenReturn(universityStudents);
		
		ArrayList<Lecture> universityLectures = new ArrayList<Lecture>();
		Lecture lecture1 = new Lecture("Math", class405, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 6, 20, 11, 30)); 
		Lecture lecture2 = new Lecture("Geography", class405, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 6, 20, 12, 30));
		Lecture lecture3 = new Lecture("Singing", class405, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 6, 20, 13, 30));
		Lecture lecture4 = new Lecture("Singing", class407, groupBBS, teacherLane, LocalDateTime.of(2017, 6, 20, 13, 30));
		universityLectures.add(lecture1);
		universityLectures.add(lecture2);
		universityLectures.add(lecture3);
		universityLectures.add(lecture4);
		lecture1 = new Lecture("Math", class405, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 6, 12, 11, 30)); 
		lecture2 = new Lecture("Geography", class465, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 6, 12, 12, 30));
		lecture3 = new Lecture("Singing", class405, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 6, 12, 13, 30));
		lecture4 = new Lecture("Singing", class407, groupBBS, teacherLane, LocalDateTime.of(2017, 6, 12, 13, 30));
		universityLectures.add(lecture1);
		universityLectures.add(lecture2);
		universityLectures.add(lecture3);
		universityLectures.add(lecture4);
		lecture1 = new Lecture("Math", class405, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 1, 12, 11, 30)); 
		lecture2 = new Lecture("Geography", class465, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 1, 12, 12, 30));
		lecture3 = new Lecture("Singing", class405, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 1, 12, 13, 30));
		lecture4 = new Lecture("Singing", class407, groupBBS, teacherLane, LocalDateTime.of(2017, 1, 12, 13, 30));
		universityLectures.add(lecture1);
		universityLectures.add(lecture2);
		universityLectures.add(lecture3);
		universityLectures.add(lecture4);
		when(lectureDao.getAll()).thenReturn(universityLectures);
		
		university = new University("Sorbonne", 6, classroomDao, studentDao, teacherDao, groupDao, lectureDao);	
	}
	
	@Test
	public void testGetTeacherMonthScheduleWithFirstCorrectTeacher() {
		ArrayList<Lecture> schedule = university.getTeacherMonthSchedule(6, teacherChernushenko);
		
		for(Lecture l : schedule) {
			assertEquals(teacherChernushenko, l.getTeacher());
		}
	}
	
	@Test
	public void testGetTeacherMonthScheduleWithSecondCorrectTeacher() {
		ArrayList<Lecture> schedule = university.getTeacherMonthSchedule(6, teacherLane);
		
		for(Lecture l : schedule) {
			assertEquals(teacherLane, l.getTeacher());
		}
	}
	
	@Test 
	public void testGetTeacherMonthScheduleWithNonUniversityTeacher() {
		Teacher nonUniversityTeacher = new Teacher("Juda", "Iscariot");
		nonUniversityTeacher.setId(-3);
		ArrayList<Lecture> schedule = university.getTeacherMonthSchedule(6, nonUniversityTeacher);
		assertNull(schedule);
	}
	
	@Test 
	public void testGetTeacherMonthScheduleWithNonExistingMonth() {
		ArrayList<Lecture> schedule = university.getTeacherMonthSchedule(354, teacherChernushenko);
		assertTrue(schedule.isEmpty());
	}
	
	@Test
	public void testGetStudentMonthScheduleWithFirstStudent() {
		Group expectedGroup = studentJobs.getGroup();
		ArrayList<Lecture> schedule = university.getStudentMonthSchedule(6, studentJobs);
		
		for(Lecture l : schedule) {
			assertEquals(expectedGroup, l.getGroup());
		}
	}
	
	@Test
	public void testGetStudentMonthScheduleWithSecondStudent() {
		Group expectedGroup = groupBBS;
		Student newStudent = new Student("Jermy", "Dunkan");
		newStudent.setGroup(groupBBS);
		//when(studentDao.insertInToDB(newStudent)).thenReturn(Integer.valueOf(46));
		university.addStudent(newStudent);
		ArrayList<Lecture> schedule = university.getStudentMonthSchedule(6, newStudent);
		
		for(Lecture l : schedule) {
			assertEquals(expectedGroup, l.getGroup());
		}
	}
	
	@Test
	public void testGetStudentMonthScheduleWithNonExistingMonth() {
		ArrayList<Lecture> schedule = university.getStudentMonthSchedule(45, studentJobs);
		assertTrue(schedule.isEmpty());
	}
	
	@Test
	public void testGetStudentMonthScheduleWithNonUniversityStudent() {
		Student wrongStudent = new Student("Kozak", "Gavrilyuk");
		wrongStudent.setId(-4);
		Group wwwGroup = new Group("WWW");
		wwwGroup.setId(-3);
		wrongStudent.setGroup(wwwGroup);
		ArrayList<Lecture> schedule = university.getStudentMonthSchedule(6, wrongStudent);
		
		assertNull(schedule);
	}
	
	@Test
	public void testGetStudentMonthScheduleFromNonExistingDaySchedules() {
		ArrayList<Lecture> schedule = university.getStudentMonthSchedule(4, studentJobs);
		
		assertTrue(schedule.isEmpty());
	}

	@Test
	public void testRemoveStudentFromUniversityStudents() {
		Student student = new Student("Petya", "Poroshenko");
		student.setGroup(groupMTZ);
		//when(studentDao.insertInToDB(student)).thenReturn(Integer.valueOf(49));
		//when(studentDao.removeFromDB(student)).thenReturn(true);
		university.addStudent(student);
		
		assertTrue(university.removeStudent(student));
	}
	
	@Test
	public void testRemoveNonUniversityStudent() {
		university.addStudent(studentJobs);
		assertFalse(university.removeStudent(new Student("Vasya", "Pupkin")));
	}
	
	@Test
	public void testRemoveUniversityTeacher() {
		Teacher testTeacher = new Teacher("Petro", "Boshirov");
		testTeacher.setId(33);
		//when(teacherDao.insertInToDB(testTeacher)).thenReturn(33);
		//when(teacherDao.removeFromDB(testTeacher)).thenReturn(true);
		university.addTeacher(testTeacher);
		assertTrue(university.removeTeacher(testTeacher));
	}
	
	@Test
	public void testRemoveNonUniversityTeacher() {
		Teacher testTeacher = new Teacher("Anton", "Geraschenko");
		testTeacher.setId(99);
		//when(teacherDao.removeFromDB(testTeacher)).thenReturn(false);
		assertFalse(university.removeTeacher(testTeacher));
	}
	
	@Test
	public void testRemoveUniversityGroup() {
		
		//when(groupDao.removeFromDB(groupBBS)).thenReturn(true);
		assertTrue(university.removeGroup(groupBBS));
	}
	
	@Test
	public void testRemoveNonUniversityGroup() {
		Group testGroupWWW = new Group("WWW");
		testGroupWWW.setId(33);
		//when(groupDao.removeFromDB(testGroupWWW)).thenReturn(false);
		assertFalse(university.removeGroup(testGroupWWW));
	}
	
	@Test
	public void testRemoveUniversityClassroom() {
		Classroom class401 = new Classroom(401);
		class401.setId(129);
		
		//when(classroomDao.removeFromDB(class401)).thenReturn(true);
		assertTrue(university.removeClassroom(class401));
	}
	
	@Test
	public void testRemoveNonExistingClassroom() {
		Classroom testClass2343 = new Classroom(2343);
		testClass2343.setId(-49);
		//when(classroomDao.removeFromDB(testClass2343)).thenReturn(false);
		assertFalse(university.removeClassroom(testClass2343));
	}
	
	@Test
	public void testAddLectureWithVacantGroupAndTeacher() {
		Classroom class401 = new Classroom(401);
		class401.setId(129);
		Lecture testLecture = new Lecture("Math", class401, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 9, 20, 11, 30));
		//when(lectureDao.insertInToDB(testLecture)).thenReturn(Integer.valueOf(32));
		assertTrue(university.addLecture(testLecture));
		ArrayList<Lecture> dayLectures = university
								         .getYearSchedule()
								         .get(testLecture.getDate().toLocalDate())
								         .getLectures();
		assertTrue(dayLectures.contains(testLecture));
	}
	
	@Test
	public void testAddLectureWithOccupiedTeacher() {
		Classroom class401 = new Classroom(401);
		class401.setId(129);
		Lecture testLecture = new Lecture("Math", class401, groupBBS, teacherChernushenko, LocalDateTime.of(2017, 6, 20, 11, 30));
		try{
			when(lectureDao.insertInToDB(testLecture)).thenReturn(null);
		} catch(Exception e) {
			e.printStackTrace();
		}
		assertFalse(university.addLecture(testLecture));
	}
	
	@Test
	public void testAddLectureWithOccupiedGroup() {
		Classroom class401 = new Classroom(401);
		class401.setId(129);
		Lecture testLecture = new Lecture("Math", class401, groupMTZ, teacherLane, LocalDateTime.of(2017, 6, 20, 11, 30));
	
		//when(lectureDao.insertInToDB(testLecture)).thenReturn(null);
		assertFalse(university.addLecture(testLecture));
	}
	
	@Test
	public void tesAddLectureWithNonUniversityTeacher() {
		Classroom class405 = new Classroom(405);
		class405.setId(126);
		Teacher nonUniversityTeacher = new Teacher("Dart", "Wejder");
		nonUniversityTeacher.setId(666);
		Lecture testLecture = new Lecture("Math", class405, groupMTZ, nonUniversityTeacher , LocalDateTime.of(2017, 9, 14, 11, 30)); 
		//when(lectureDao.insertInToDB(testLecture)).thenReturn(null);
		assertFalse(university.addLecture(testLecture));
	}
	
	@Test
	public void tesAddLectureWithNonUniversityGroup() {
		Classroom class405 = new Classroom(405);
		class405.setId(126);
		Group wrongGroup = new Group("WWW");
		wrongGroup.setId(-5);
		Lecture testLecture = new Lecture("Math", class405, wrongGroup, teacherChernushenko, LocalDateTime.of(2017, 9, 14, 11, 30)); 
		//when(lectureDao.insertInToDB(testLecture)).thenReturn(null);
		assertFalse(university.addLecture(testLecture));
	}
	
	@Test
	public void tesAddLectureWithNonUniversityClassroom() {
		Classroom wrongClassroom = new Classroom(666);
		wrongClassroom.setId(-8);
		Lecture testLecture = new Lecture("Math", wrongClassroom, groupMTZ, teacherChernushenko, LocalDateTime.of(2017, 9, 14, 11, 30)); 
		//LectureDao lectureDao = mock(LectureDao.class);
		//when(lectureDao.insertInToDB(testLecture)).thenReturn(null);
		assertFalse(university.addLecture(testLecture));
	}
	
	@Test
	public void testAddStudentWithNonUniversityGroup() {
		Student wrongGroupStudent = new Student("Donald", "Frankenshtain");
		Group tempGroup = new Group("WWW");
		tempGroup.setId(-5);
		assertFalse(university.addStudent(wrongGroupStudent));
	}
	
	@Test
	public void testGetStudentDayScheduleWithNonUniversityStudent() {
		Student nonUniversityStudent = new Student("Donald", "Frankenshtain");
		assertNull(university.getStudentDaySchedule(LocalDate.of(2017, 6, 20), nonUniversityStudent));
	}
	
	@Test
	public void testGetTeacherDayScheduleWithNonUniversityTeacher() {
		Teacher nonUniversityTeacher = new Teacher("Pontij", "Pilat");
		ArrayList<Lecture> schedule = university.getTeacherDaySchedule(LocalDate.of(2017, 6, 20), nonUniversityTeacher);
		assertNull(schedule);
	}
	
	@Test
	public void testGroupAddingAndRemoving() {
		Group testGroup = new Group("BFR");
		testGroup.setId(33);
		//when(groupDao.insertInToDB(testGroup)).thenReturn(33);
		//when(groupDao.removeFromDB(testGroup)).thenReturn(true).thenReturn(false);
		assertTrue(university.addGroup(testGroup));
		
		for (Group g : university.getGroups()) {
			if ("BFR".equals(g.getName())) {
				testGroup = g;
			}
		}
		
		assertTrue(university.getGroups().contains(testGroup));
		assertTrue(university.removeGroup(testGroup));
		assertFalse(university.removeGroup(testGroup));
		
		
	}
	
	@Test
	public void testStudentAddingAndRemoving() {
		Student testStudent = new Student("Volfgangs", "Mocarts");
		testStudent.setGroup(groupMTZ);
		testStudent.setId(44);
		//when(studentDao.insertInToDB(testStudent)).thenReturn(44);
		//when(studentDao.removeFromDB(testStudent)).thenReturn(true).thenReturn(false);
		assertTrue(university.addStudent(testStudent));
		
		for (Student s : university.getStudents()) {
			if (s.getFullName().equals(testStudent.getFullName())) {
				testStudent = s;
			}
		}
		
		assertTrue(university.getStudents().contains(testStudent));
		assertTrue(university.removeStudent(testStudent));
		assertFalse(university.removeStudent(testStudent));
	}
	
	@Test
	public void testTeacherAddingAndRemoving() {
		Teacher testTeacher = new Teacher("Fridrih", "Nitzsche");
		testTeacher.setId(44);
		//when(teacherDao.insertInToDB(testTeacher)).thenReturn(44);
		//when(teacherDao.removeFromDB(testTeacher)).thenReturn(true).thenReturn(false);
		assertTrue(university.addTeacher(testTeacher));
		
		for (Teacher t : university.getTeachers()) {
			if (t.getFullName().equals(testTeacher.getFullName())) {
				testTeacher = t;
			}
		}
		
		assertTrue(university.getTeachers().contains(testTeacher));
		assertTrue(university.removeTeacher(testTeacher));
		assertFalse(university.removeTeacher(testTeacher));
	}
	
	@Test
	public void testClassroomAddingAndRemoving() {
		Classroom testClassroom = new Classroom(570);
		//when(classroomDao.insertInToDB(testClassroom)).thenReturn(77);
		//when(classroomDao.removeFromDB(testClassroom)).thenReturn(true).thenReturn(false);
		assertTrue(university.addClassroom(testClassroom));
		
		for (Classroom c : university.getClassrooms()) {
			if (c.getNumber() == 570) {
				testClassroom = c;
			}
		}
		
		assertTrue(university.getClassrooms().contains(testClassroom));
		assertTrue(university.removeClassroom(testClassroom));
		assertFalse(university.removeClassroom(testClassroom));
		
	}
	
}

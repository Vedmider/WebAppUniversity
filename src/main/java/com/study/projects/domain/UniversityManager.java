package com.study.projects.domain;

import java.util.ArrayList;

import com.study.projects.dao.StudentDao;
import com.study.projects.dao.UniversityDBAccessException;
import com.study.projects.dao.UniversityDao;

public class UniversityManager {
		
	
	
	public University insert(String universityName) {
		University univ = null;
		UniversityDao dao = new UniversityDao();
		try {
			univ = dao.insertInToDB(universityName);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
		return univ;
	}
	
	public boolean update(String universityName, int university_id) {
		boolean isUpdated = false;
		University univ = new University(universityName, university_id);
		UniversityDao dao = new UniversityDao();
		try {
			isUpdated = dao.update(univ);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
		
		return isUpdated;
	}
	
	public University getById(Integer university_id) {
		UniversityDao dao = new UniversityDao();
		University univ = null;
		try {
			univ = dao.getByPK(university_id);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
		
		return univ;
	}
	
	public boolean delete(String universityName, int university_id) {
		boolean isDeleted = false;
		University univ = new University(universityName, university_id);
		UniversityDao dao = new UniversityDao();
		try {
			isDeleted = dao.removeFromDB(univ);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
		return isDeleted;
	}
	
	public ArrayList<University> getAll(){
		ArrayList<University> univList = null;
		UniversityDao dao = new UniversityDao();
		try {
			univList = dao.getAll();
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 	
		return univList;
	}
	
	public boolean addStudentToUniversity(University univ, Student student) {
		StudentDao studentDao = univ.getStudentDao();
		
		if(univ.getGroups().contains(student.getGroup())) {
			
			Integer studentId = null;; 
			try{
				studentId = studentDao.insertInToDB(student);
			} catch (UniversityDBAccessException e) {
				e.printStackTrace();
			} 
			
			if(studentId != null) {
				student.setId(studentId);
			}
			
			return univ.addStudent(student);
		}
		return false;
	}
	
	public boolean removeStudentFromUniversity(University univ, Student student){
		boolean isRemoved = false;
		try {
			isRemoved = univ.getStudentDao().removeFromDB(student);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
		return univ.removeStudent(student) && isRemoved;
	}
	
	public boolean addTeacherToUniversity(University univ, Teacher teacher) {
		
		Integer teacherId = null;
		try {		
			teacherId = univ.getTeacherDao().insertInToDB(teacher);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
		
		if(teacherId != null) {
			teacher.setId(teacherId);
			return univ.addTeacher(teacher);
		}
		
		return false;
	}	
	
	public boolean removeTeacherFromUniversity(University univ, Teacher teacher) {
		boolean isRemoved = false;
		try {
			isRemoved = univ.getTeacherDao().removeFromDB(teacher); 
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		} 
		return univ.removeTeacher(teacher) && isRemoved;
	}
	
	public boolean addGroupToUniversity(University univ, Group group) {
		Integer groupId = null;
		try {		
			groupId = univ.getGroupDao().insertInToDB(group);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		if (groupId != null) {
			group.setId(groupId);
			return univ.addGroup(group);
		}
		return false;
	}

	public boolean removeGroupFromUniversity(University univ, Group group) {
		boolean isRemoved = false;
		try {
			isRemoved = univ.getGroupDao().removeFromDB(group);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return univ.removeGroup(group) && isRemoved;
	}
	
	public boolean addClassroomToUniversity(University univ, Classroom classroom) {
		Integer classroomId = null;
		try { 
			classroomId = univ.getClassroomDao().insertInToDB(classroom);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		if (classroomId != null) {
			classroom.setId(classroomId);
			return univ.addClassroom(classroom);
		}
		
		 return false;
	}

	public boolean removeClassroomFromUniversity(University univ, Classroom classroom) {
		boolean isRemoved = false;
		try {
			isRemoved = univ.getClassroomDao().removeFromDB(classroom);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return univ.removeClassroom(classroom) && isRemoved;
	}
	
	public boolean addLecture(University univ, Lecture lecture) {
		Integer lectureId = null;
		try { 
			lectureId = univ.getLectureDao().insertInToDB(lecture);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		if (lectureId != null) {
			lecture.setId(lectureId);
			return univ.addLecture(lecture);
		}
		
		return false;
	}
	
	public boolean removeLectureFromUniversity(University univ, Lecture lecture) {
		boolean isRemoved = false;
		try {
			isRemoved = univ.getLectureDao().removeFromDB(lecture);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return  isRemoved && univ.removeLecture(lecture);
	}
	
	public boolean updateStudent(University univ, Student student) {
		boolean isUpdated = false;
		try {
			isUpdated = univ.getStudentDao().update(student);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return isUpdated;
	}
	
	public boolean updateTeacher(University univ, Teacher teacher) {
		boolean isUpdated = false;
		try {
			isUpdated = univ.getTeacherDao().update(teacher);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return isUpdated; 
	}
	
	public boolean updateClassroom(University univ, Classroom classroom) {
		boolean isUpdated = false;
		try {
			isUpdated = univ.getClassroomDao().update(classroom);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return isUpdated; 
		 
	}
	
	public boolean updateGroup(University univ, Group group) {
		boolean isUpdated = false;
		try {
			isUpdated = univ.getGroupDao().update(group);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return isUpdated;
	}
	
	public boolean updateLecture(University univ, Lecture lecture) {
		boolean isUpdated = false;
		try {
			isUpdated = univ.getLectureDao().update(lecture);
		} catch (UniversityDBAccessException e) {
			e.printStackTrace();
		}
		
		return isUpdated; 
	}
	

}

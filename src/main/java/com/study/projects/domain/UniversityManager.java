package com.study.projects.domain;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.study.projects.dao.StudentDao;
import com.study.projects.dao.UniversityDBAccessException;
import com.study.projects.dao.UniversityDao;

public class UniversityManager {
	
	private static final Logger logger = LoggerFactory.getLogger(UniversityManager.class);	
	
	public University insert(String universityName) {
		University univ = null;
		UniversityDao dao = new UniversityDao();
		try {
			univ = dao.insertInToDB(universityName);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not insert University " + universityName + " to DB", e);
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
			logger.error("Can not update University " + universityName, e);
		} 
		
		return isUpdated;
	}
	
	public University getById(Integer university_id) {
		UniversityDao dao = new UniversityDao();
		University univ = null;
		try {
			univ = dao.getByPK(university_id);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not get University " + university_id + " by id", e);
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
			logger.error("Can not delete " + universityName + " with ID " + university_id, e);
		} 
		return isDeleted;
	}
	
	public ArrayList<University> getAll(){
		ArrayList<University> univList = null;
		UniversityDao dao = new UniversityDao();
		try {
			univList = dao.getAll();
		} catch (UniversityDBAccessException e) {
			logger.error("Can not get full Universities list.", e);
		} 	
		return univList;
	}
	
	public boolean addStudentToUniversity(University univ, Student student) {
		StudentDao studentDao = univ.getStudentDao();
		
		logger.info("adding student {} to University {}", student.getFullName(), univ.getName());
		
		if(univ.getGroups().contains(student.getGroup())) {
			
			Integer studentId = null;; 
			try{
				studentId = studentDao.insertInToDB(student);
			} catch (UniversityDBAccessException e) {
				logger.error("Cannot add student " + student.getFullName() + " to DB", e);
			} 
			
			if(studentId != null) {
				student.setId(studentId);
			}
			
			logger.info("student {} succesfully created with ID {}", student.getFullName(), studentId);
			return univ.addStudent(student);
		}
		return false;
	}
	
	public boolean removeStudentFromUniversity(University univ, Student student){
		boolean isRemoved = false;
		
		logger.info("removing student {} from University {}", student.getFullName(), univ.getName());
		try {
			isRemoved = univ.getStudentDao().removeFromDB(student);
		} catch (UniversityDBAccessException e) {
			logger.error("Something went wrong while deleting student " + student.getFullName(), e);
		} 
		return univ.removeStudent(student) && isRemoved;
	}
	
	public boolean addTeacherToUniversity(University univ, Teacher teacher) {
		Integer teacherId = null;
		
		logger.info("adding teacher {} to University {}", teacher.getFullName(), univ.getName());
		try {		
			teacherId = univ.getTeacherDao().insertInToDB(teacher);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not add teacher " + teacher.getFullName(), e);
		} 
		
		if(teacherId != null) {
			teacher.setId(teacherId);
			
			logger.info("teacher succesfully created with ID {}", teacherId);
			return univ.addTeacher(teacher);
		}
		
		return false;
	}	
	
	public boolean removeTeacherFromUniversity(University univ, Teacher teacher) {
		boolean isRemoved = false;
		
		logger.info("removing teacher {} from University {}", teacher.getFullName(), univ.getName());
		try {
			isRemoved = univ.getTeacherDao().removeFromDB(teacher); 
		} catch (UniversityDBAccessException e) {
			logger.error("Can not delete teacher " + teacher.getFullName(), e);
		} 
		return univ.removeTeacher(teacher) && isRemoved;
	}
	
	public boolean addGroupToUniversity(University univ, Group group) {
		Integer groupId = null;
		
		logger.info("adding group {} to University {}", group.getName(), univ.getName());
		try {		
			groupId = univ.getGroupDao().insertInToDB(group);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not add group " + group.getName() + " to University", e);
		}
		
		if (groupId != null) {
			group.setId(groupId);
			logger.info("succesfully added group {} t University {}", group.getName(), univ.getName());
			return univ.addGroup(group);
		}
		return false;
	}

	public boolean removeGroupFromUniversity(University univ, Group group) {
		boolean isRemoved = false;
		
		logger.info("removing group {} from University {}", group.getName(), univ.getName());
		try {
			isRemoved = univ.getGroupDao().removeFromDB(group);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not remove group " + group.getName(), e);
		}
		
		return univ.removeGroup(group) && isRemoved;
	}
	
	public boolean addClassroomToUniversity(University univ, Classroom classroom) {
		Integer classroomId = null;
		
		logger.info("adding classrom {} to University {}", classroom.getNumber(), univ.getName());
		try { 
			classroomId = univ.getClassroomDao().insertInToDB(classroom);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not add classroom " + classroom.getNumber(), e);
		}
		
		if (classroomId != null) {
			classroom.setId(classroomId);
			logger.info("classroom {} created with ID {}", classroom.getNumber(), classroomId);
			return univ.addClassroom(classroom);
		}
		
		 return false;
	}

	public boolean removeClassroomFromUniversity(University univ, Classroom classroom) {
		boolean isRemoved = false;
		
		logger.info("removing classroom {} from University {}", classroom.getNumber(), univ.getName());
		try {
			isRemoved = univ.getClassroomDao().removeFromDB(classroom);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not remove classroom " + classroom.getNumber(), e);
		}
		
		return univ.removeClassroom(classroom) && isRemoved;
	}
	
	public boolean addLecture(University univ, Lecture lecture) {
		Integer lectureId = null;
		
		logger.info("adding lecture {} to University {}", lecture.getSubject(), univ);
		try { 
			lectureId = univ.getLectureDao().insertInToDB(lecture);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not add lecture " + lecture.getSubject(), e);
		}
		
		if (lectureId != null) {
			lecture.setId(lectureId);
			logger.info("lecture {} successfully created with ID {}", lecture.getSubject(), lectureId);
			return univ.addLecture(lecture);
		}
		
		return false;
	}
	
	public boolean removeLectureFromUniversity(University univ, Lecture lecture) {
		boolean isRemoved = false;
		
		logger.info("removing lecture {} from University {}", lecture.getSubject(), univ.getName());
		try {
			isRemoved = univ.getLectureDao().removeFromDB(lecture);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not remove lecture " + lecture.getSubject(), e);
		}
		
		return  isRemoved && univ.removeLecture(lecture);
	}
	
	public boolean updateStudent(University univ, Student student) {
		boolean isUpdated = false;
		
		logger.info("updating student {} from University {}", student.getFullName(), univ.getName());
		try {
			isUpdated = univ.getStudentDao().update(student);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not update student " + student.getFullName(), e);
		}
		
		return isUpdated;
	}
	
	public boolean updateTeacher(University univ, Teacher teacher) {
		boolean isUpdated = false;
		
		logger.info("updating teacher {} from University {}", teacher.getFullName(), univ.getName());
		try {
			isUpdated = univ.getTeacherDao().update(teacher);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not update teacher " + teacher.getFullName(), e);
		}
		
		return isUpdated; 
	}
	
	public boolean updateClassroom(University univ, Classroom classroom) {
		boolean isUpdated = false;
		
		logger.info("updating classroom N {} in University {}", classroom.getNumber(), univ.getName());
		try {
			isUpdated = univ.getClassroomDao().update(classroom);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not update classroom N " + classroom.getNumber(), e);
		}
		
		return isUpdated; 
		 
	}
	
	public boolean updateGroup(University univ, Group group) {
		boolean isUpdated = false;
		
		logger.info("updating group {} from University {}", group.getName(), univ.getName());
		try {
			isUpdated = univ.getGroupDao().update(group);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not update group " + group.getName(), e);
		}
		
		return isUpdated;
	}
	
	public boolean updateLecture(University univ, Lecture lecture) {
		boolean isUpdated = false;
		
		logger.info("updating lecture {} in University {}", lecture.getSubject(), univ.getName());
		try {
			isUpdated = univ.getLectureDao().update(lecture);
		} catch (UniversityDBAccessException e) {
			logger.error("Can not update lecture " + lecture.getSubject(), e);
		}
		
		return isUpdated; 
	}
	

}

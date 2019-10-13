package com.study.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.study.projects.domain.*;

public class LectureDao extends DAOParent<Lecture, Integer> {
	private int universityId;
	private static final String insertQuery = "INSERT INTO lectures (subject, classroom, teacher_id, date, group_id, university_id)"
												+ " VALUES (?, ?, ?, ?, ?, ?)";
	private static final String updateQuery = "UPDATE lectures SET teacher_id = ?, date = ?, group_id = ? "
												+ "WHERE id = ?";
	private static final String retrieveQuery =	"SELECT lectures.subject, "
													 + "lectures.classroom, "
													 + "lectures.date, "
													 + "lectures.group_id, "
													 + "lectures.teacher_id, "
													 + "lectures.id, "
													 + "groups.name, "
													 + "teachers.full_name, "													
													 + "classrooms.class_number "
												+ "FROM lectures, groups, teachers, classrooms "
												+ "WHERE lectures.teacher_id = teachers.id AND  "
													 + "lectures.group_id = groups.id AND "
													 + "lectures.classroom = classrooms.id AND "
													 + "lectures.id = ?";
	private static final String deleteQuery = "DELETE FROM lectures "
												+ "WHERE id = ?";
	private static final String getAllQuery = "SELECT lectures.subject, "
													+ "lectures.classroom, "
													+ "lectures.date, "
													+ "lectures.group_id, "
													+ "lectures.teacher_id, "
													+ "lectures.id, "
													+ "groups.name, "
													+ "teachers.full_name, "													
													+ "classrooms.class_number "
											+ "FROM lectures, groups, teachers, classrooms "
											+ "WHERE lectures.teacher_id = teachers.id AND  "
													+ "lectures.group_id = groups.id AND "
													+ "lectures.classroom = classrooms.id AND "
													+ "lectures.university_id =  ";
	
	public LectureDao(int universityId) {
		this.universityId = universityId;
		
	}

	@Override
	protected String getInsertSQL() {
		return insertQuery;
	}

	@Override
	protected String getUpdateSQL() {
		return updateQuery;
	}

	@Override
	protected String getRetrieveSQL() {
		return retrieveQuery;
	}

	@Override
	protected String getDeleteSQL() {
		return deleteQuery;
	}

	@Override
	protected String getAllSQL() {
		return getAllQuery + universityId;
	}

	@Override
	protected void statementForInsert(PreparedStatement statement, Lecture object) throws SQLException {
		statement.setString(1, object.getSubject());
		statement.setInt(2, object.getClassroom().getId());
		statement.setInt(3, object.getTeacher().getId());
		statement.setObject(4, object.getDate());
		statement.setInt(5, object.getGroup().getId());
		statement.setInt(6, universityId);
		
	}

	@Override
	protected void statementForUpdate(PreparedStatement statement, Lecture object) throws SQLException {
		statement.setInt(1, object.getTeacher().getId());
		statement.setObject(2, object.getDate());
		statement.setInt(3, object.getGroup().getId());
		statement.setInt(4, object.getId());
		
	}

	@Override
	protected void statementForRetrieve(PreparedStatement statement, Integer key) throws SQLException {
		statement.setInt(1, key.intValue());
		
		
	}

	@Override
	protected void statementForRemove(PreparedStatement statement, Lecture object) throws SQLException {
		statement.setInt(1, object.getId());
		
	}

	@Override
	protected Lecture parseRetrieveSQL(ResultSet resultSet) throws SQLException {
		Lecture lecture = null;
		
		while(resultSet.next()) {
			Classroom room = new Classroom(resultSet.getInt("classroom"));
			Group group = new Group(resultSet.getString("name"));
			group.setId(resultSet.getInt("group_id"));
			String[] str = resultSet.getString("full_name").split(" ");
			Teacher teacher = new Teacher(str[0], str[1]);
			teacher.setId(resultSet.getInt("teacher_id"));
			lecture = new Lecture(resultSet.getString("subject"), room, group, teacher, resultSet.getObject("date", LocalDateTime.class));
		}
		return lecture;
	}

	@Override
	protected ArrayList<Lecture> parseGetAllSQL(ResultSet resultSet) throws SQLException {
		Lecture lecture = null;
		ArrayList<Lecture> lectureList = new ArrayList<>();
		
		while(resultSet.next()) {
			Classroom room = new Classroom(resultSet.getInt("classroom"));
			Group group = new Group(resultSet.getString("name"));
			group.setId(resultSet.getInt("group_id"));
			String[] str = resultSet.getString("full_name").split(" ");
			Teacher teacher = new Teacher(str[0], str[1]);
			teacher.setId(resultSet.getInt("teacher_id"));
			lecture = new Lecture(resultSet.getString("subject"), room, group, teacher, resultSet.getObject("date", LocalDateTime.class));
			lectureList.add(lecture);
		}
		return lectureList;
	}

}

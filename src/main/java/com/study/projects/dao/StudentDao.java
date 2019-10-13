package com.study.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.study.projects.domain.Student;
import com.study.projects.domain.Group;

public class StudentDao extends DAOParent<Student, Integer>{
	private int universityId;
	private static final String insertQuery = "INSERT INTO students (full_name, group_id, university_id) VALUES (?, ?, ?)";
	private static final String updateQuery = "UPDATE students SET full_name = ? WHERE id = ?";
	private static final String retrieveQuery = "SELECT students.*, groups.* FROM students JOIN groups ON groups.id = students.group_id WHERE students.id = ?";
	private static final String deleteQuery = "DELETE FROM students WHERE id = ?";
	private static final String getAllQuery = "SELECT students.*, groups.*  FROM students JOIN groups ON groups.id = students.group_id WHERE students.university_id = ";

	public StudentDao(int universityId) {
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
	protected void statementForInsert(PreparedStatement statement, Student object) throws SQLException {
		statement.setString(1, object.getFullName());
		statement.setInt(2, object.getGroup().getId());
		statement.setInt(3, universityId);
		
	}

	@Override
	protected void statementForUpdate(PreparedStatement statement, Student object) throws SQLException {
		statement.setString(1, object.getFullName());
		statement.setInt(2, object.getId());
	}

	@Override
	protected void statementForRetrieve(PreparedStatement statement, Integer key) throws SQLException {
		statement.setInt(1, key.intValue());
		
	}

	@Override
	protected void statementForRemove(PreparedStatement statement, Student object) throws SQLException {
		statement.setInt(1, object.getId());	
	}

	@Override
	protected Student parseRetrieveSQL(ResultSet resultSet) throws SQLException {
		Student stud = null;
		
		while(resultSet.next()) {
			String[] str = resultSet.getString("students.full_name").trim().split(" ");
			stud = new Student (str[0], str[1]);
			Group g = new Group(resultSet.getString("groups.name"));
			g.setId(resultSet.getInt("students.group_id"));
			stud.setGroup(g);
			stud.setId(resultSet.getInt("students.id"));
		}
		
		return stud;
	}

	@Override
	protected ArrayList<Student> parseGetAllSQL(ResultSet resultSet) throws SQLException {
		ArrayList<Student> studList = new ArrayList<>();
		
		while(resultSet.next()) {
			String[] str = resultSet.getString("students.full_name").trim().split(" ");
			Student stud = new Student (str[0], str[1]);
			Group g = new Group(resultSet.getString("groups.name"));
			g.setId(resultSet.getInt("students.group_id"));
			stud.setGroup(g);
			stud.setId(resultSet.getInt("students.id"));
			studList.add(stud);
		}
		
		return studList;
	}

}

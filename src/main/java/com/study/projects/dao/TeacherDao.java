package com.study.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.study.projects.domain.Teacher;

public class TeacherDao extends DAOParent<Teacher, Integer>{
	private int universityId;
	private static final String insertQuery = "INSERT INTO teachers (full_name, university_id) VALUES (?, ?)";
	private static final String updateQuery = "UPDATE teachers SET full_name = ? WHERE id = ?";
	private static final String retrieveQuery = "SELECT * FROM teachers WHERE id = ?";
	private static final String deleteQuery = "DELETE FROM teachers WHERE id = ?";
	private static final String getAllQuery = "SELECT * FROM teachers WHERE university_id = ";

	public TeacherDao(int universityId) {
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
	protected void statementForInsert(PreparedStatement statement, Teacher object) throws SQLException {
		statement.setString(1, object.getFullName());
		statement.setInt(2, universityId);
		
	}

	@Override
	protected void statementForUpdate(PreparedStatement statement, Teacher object) throws SQLException {
		statement.setString(1, object.getFullName());
		statement.setInt(2, object.getId());
		
	}

	@Override
	protected void statementForRetrieve(PreparedStatement statement, Integer key) throws SQLException {
		statement.setInt(1, key.intValue());
		
	}

	@Override
	protected void statementForRemove(PreparedStatement statement, Teacher object) throws SQLException {
		statement.setInt(1, object.getId());
		
	}

	@Override
	protected Teacher parseRetrieveSQL(ResultSet resultSet) throws SQLException {
		Teacher teacher = null;
		
		while (resultSet.next()) {
			String[] str = resultSet.getString("full_name").split(" ");
			teacher = new Teacher(str[0], str[1]);
			teacher.setId(resultSet.getInt("id"));
		}
		return teacher;
	}

	@Override
	protected ArrayList<Teacher> parseGetAllSQL(ResultSet resultSet) throws SQLException {
		ArrayList<Teacher> list = new ArrayList<>();
		Teacher teacher = null;
		
		while (resultSet.next()) {
			String[] str = resultSet.getString("full_name").split(" ");
			teacher = new Teacher(str[0], str[1]);
			teacher.setId(resultSet.getInt("id"));
			list.add(teacher);
		}
		return list;
	}

}

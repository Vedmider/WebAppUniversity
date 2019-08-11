package com.study.projects.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.study.projects.domain.Group;

public class GroupDao extends DAOParent<Group, Integer> {
	private int universityId;
	private static final String insertQuery = "INSERT INTO groups (name, university_id) VALUES (?, ?)";
	private static final String updateQuery = "UPDATE groups SET name = ? WHERE id = ?";
	private static final String retrieveQuery = "SELECT name, id FROM groups WHERE id = ?";
	private static final String deleteQuery = "DELETE FROM groups WHERE id = ?";
	private static final String getAllQuery = "SELECT * FROM groups WHERE university_id = ";
	
	public GroupDao(int universityId) {
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
	protected void statementForInsert(PreparedStatement statement, Group object) throws SQLException {
		statement.setString(1, object.getName());
		statement.setInt(2, universityId);
	}

	@Override
	protected void statementForUpdate(PreparedStatement statement, Group object) throws SQLException {
		statement.setString(1, object.getName());	
		statement.setInt(2, object.getId());
	}

	@Override
	protected void statementForRemove(PreparedStatement statement, Group object) throws SQLException {
		statement.setInt(1, object.getId());	
	}

	@Override
	protected Group parseRetrieveSQL(ResultSet resultSet) throws SQLException {
		Group g = null;
		while (resultSet.next()) {
            g = new Group(resultSet.getString("name"));
            g.setId(resultSet.getInt("id"));
        }
		return g;
	}

	@Override
	protected ArrayList<Group> parseGetAllSQL(ResultSet resultSet) throws SQLException {
		ArrayList<Group> list = new ArrayList<Group>();
		while(resultSet.next()) {
			Group g = new Group(resultSet.getString("name"));
			g.setId(resultSet.getInt("id"));
			list.add(g);
		}
		return list;
	}

	@Override
	protected void statementForRetrieve(PreparedStatement statement, Integer key) throws SQLException {
		statement.setInt(1, key);
		
	}

}

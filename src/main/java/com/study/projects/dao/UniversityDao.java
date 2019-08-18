package com.study.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.study.projects.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversityDao {
	private final static Logger logger = LoggerFactory.getLogger(UniversityDao.class);
	protected DaoFactory daoFactory = DaoFactory.getInstance();
	private static final String insertQuery = "INSERT INTO universities (university_name) VALUES (?)";
	private static final String updateQuery = "UPDATE universities SET university_name = ? WHERE university_id = ?";
	private static final String retrieveQuery = "SELECT * FROM universities WHERE university_id = ?";
	private static final String deleteQuery = "DELETE FROM universities WHERE university_id = ?";


	protected String getInsertSQL() {
		return insertQuery;
	}

	protected String getUpdateSQL() {
		return updateQuery;
	}

	protected String getRetrieveSQL() {
		return retrieveQuery;
	}

	protected String getDeleteSQL() {
		return deleteQuery;
	}

	protected void statementForUpdate(PreparedStatement statement, University object) throws SQLException {
		statement.setString(1, object.getName());
		statement.setInt(2, object.getId());
		
	}

	protected void statementForRetrieve(PreparedStatement statement, Integer key) throws SQLException {
		statement.setInt(1, key);
		
	}

	protected void statementForRemove(PreparedStatement statement, University object) throws SQLException {
		statement.setInt(1, object.getId());
		
	}

	protected University parseRetrieveSQL(ResultSet resultSet) throws SQLException {
		University university = null;
		
		while(resultSet.next()) {
			university = new University(resultSet.getString("university_name"), resultSet.getInt("university_id"));
		}
		return university;
	}

	protected ArrayList<University> parseGetAllSQL(ResultSet resultSet) throws SQLException {
		ArrayList<University> universityList = new ArrayList<>();
		University university = null;
		
		while(resultSet.next()) {
			System.out.println(resultSet.getFetchSize());
			university = new University(resultSet.getString("university_name"), resultSet.getInt("university_id"));
			
			
			universityList.add(university);
		}
		return universityList;
	}
	
	public University insertInToDB(String universityName) throws UniversityDBAccessException{
		String query = getInsertSQL();
		University univ = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowsAffected;
		Integer generatedId = null;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, universityName);
			rowsAffected = statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			if(rowsAffected != 1) {
				logger.info("1 row was not affected");
			}
			
			while(resultSet.next()) {
				generatedId = Integer.valueOf(resultSet.getInt("university_id"));
					
			}
			
		} catch(SQLException e) {
			throw new UniversityDBAccessException("Cannot insert to database", e);
		} finally {
			try{
				if (statement != null) {
					statement.close();
					logger.debug("Statement close");
				}
				
				if(con != null) {
					con.close();
					logger.debug("Connection close");
				}
			} catch (SQLException e) {
				throw new UniversityDBAccessException("cannot close connection", e);
			}	
		}
		univ = new University(universityName, generatedId);
		return univ;
	}
	
	public boolean update(University object) throws UniversityDBAccessException{
		String query = getUpdateSQL();
		Connection con = null;
		PreparedStatement statement = null;
		int rowsAffected;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query);
			statementForUpdate(statement, object);
			rowsAffected = statement.executeUpdate();
			
			if(rowsAffected == 1) {
				return true;
			} else {
				logger.info("1 row was not affected");
			}
			
		} catch(SQLException e) {
			throw new UniversityDBAccessException("Cannot update database", e);
		} finally {
			try{
				if (statement != null) {
					statement.close();
					logger.debug("Statement close");
				}
				
				if(con != null) {
					con.close();
					logger.debug("Connection close");
				}
				
			} catch (SQLException e) {
				throw new UniversityDBAccessException("cannot close connection", e);
			}	
		}
		
		return false;	
	}
	
	public University getByPK(Integer key) throws UniversityDBAccessException {
		String query = getRetrieveSQL();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		University object = null;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query);
			statementForRetrieve(statement, key);
			resultSet = statement.executeQuery();
			object = parseRetrieveSQL(resultSet);
		} catch (SQLException e) {
			throw new UniversityDBAccessException("Cannot retrieve from database", e);
		} finally {
			try{
				if(resultSet != null) {
					resultSet.close();
					logger.debug("ResultSet close");
				}
				
				if (statement != null) {
					statement.close();
					logger.debug("Statement close");
				}
				
				if(con != null) {
					con.close();
					logger.debug("Connection close");
				}	
			} catch (SQLException e) {
				throw new UniversityDBAccessException("cannot close connection", e);
			}	
		}
		return object;
	}
	
	public boolean removeFromDB(University object) throws UniversityDBAccessException {
		String query = getDeleteSQL();
		Connection con = null;
		PreparedStatement statement = null;
		int rowsAffected;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query);
			statementForRemove(statement, object);
			rowsAffected = statement.executeUpdate();
			
			if(rowsAffected == 1) {
				return true;
			} else {
				logger.debug("1 row was not affected");
			}
			
		} catch(SQLException e) {
			throw new UniversityDBAccessException("Cannot delete", e);
		} finally {
			try{
				if (statement != null) {
					statement.close();
					logger.debug("Statement close");
				}
				
				if(con != null) {
					con.close();
					logger.debug("Connection close");
				}
				
			} catch (SQLException e) {
				throw new UniversityDBAccessException("cannot close connection", e);
			}	
		}
		
		return false;	
	}
	
	public ArrayList<University> getAll() throws UniversityDBAccessException {
		String query = "SELECT * FROM universities";
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<University> listT = null;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query);
			resultSet = statement.executeQuery();
			listT = parseGetAllSQL(resultSet);
		} catch (SQLException e) {
			throw new UniversityDBAccessException("Cannot get all from database", e);
		} finally {
			try{
				if(resultSet != null) {
					resultSet.close();
					logger.debug("ResultSet close");
				}
				
				if (statement != null) {
					statement.close();
					logger.debug("Statement close");
				}
				
				if(con != null) {
					con.close();
					logger.debug("Connection close");
				}	
			} catch (SQLException e) {
				throw new UniversityDBAccessException("cannot close connection", e);
			}	
		}
		return listT;
	}

}

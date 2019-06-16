package com.study.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.study.projects.domain.Classroom;

public class ClassroomDao extends DAOParent<Classroom, Integer>{
	private int universityId;
	private static final Logger logger = LoggerFactory.getLogger(ClassroomDao.class);
	private static final String insertQuery = "INSERT INTO classrooms (class_number, university_id) VALUES (?, ?)";
	private static final String updateQuery = "UPDATE classrooms SET class_number = ? WHERE id = ?";
	private static final String retrieveQuery = "SELECT class_number FROM classrooms WHERE id = ?";
	private static final String deleteQuery = "DELETE FROM classrooms WHERE id = ?";
	private static final String getAllQuery = "SELECT * FROM classrooms WHERE university_id = ";
	
	public ClassroomDao(int universityId) {
		this.universityId = universityId;
	}
	
	public Integer insertInToDB(Classroom object) throws UniversityDBAccessException{
		String query = getInsertSQL();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int rowsAffected;
		Integer generatedId = null;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statementForInsert(statement, object);
			rowsAffected = statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			if(rowsAffected != 1) {
				logger.info("1 row was not affected");
			}
			
			while(resultSet.next()) {
				generatedId = Integer.valueOf(resultSet.getInt("id"));
					
			}
			
		} catch(SQLException e) {
			throw new UniversityDBAccessException("Cannot insert to database", e);
		} finally {
			try{
				if (statement != null) {
					statement.close();
					logger.debug("Statement close");				}
				
				if(con != null) {
					con.close();
					logger.debug("Connection close");
				}
			} catch (SQLException e) {
				throw new UniversityDBAccessException("Cannot insert to database", e);
			}	
		}
		return generatedId;
	}
	
	public boolean update(Classroom object) throws UniversityDBAccessException {
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
			throw new  UniversityDBAccessException("Cannot update database", e);
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
				throw new  UniversityDBAccessException("Cannot cose connection", e);
			}	
		}
		
		return false;	
	}
	
	public Classroom getByPK(Integer key) throws UniversityDBAccessException {
		String query = getRetrieveSQL();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Classroom object = null;
		
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
	
	public boolean removeFromDB(Classroom object) throws UniversityDBAccessException {
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
				logger.info("1 row was not affected");
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
	
	public ArrayList<Classroom> getAll() throws UniversityDBAccessException {
		String query = getAllSQL();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<Classroom> listT = null;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query);
			resultSet = statement.executeQuery();
			listT = parseGetAllSQL(resultSet);
		} catch (SQLException e) {
			throw new UniversityDBAccessException("Cannot get from database", e);
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
	protected Classroom parseRetrieveSQL(ResultSet resultSet) throws SQLException{
		Classroom classroom = null;
		while (resultSet.next()) {
            classroom = new Classroom(resultSet.getInt("class_number"));
            classroom.setId(resultSet.getInt("id"));
        }
		return classroom;
	}

	@Override
	protected ArrayList<Classroom> parseGetAllSQL(ResultSet resultSet) throws SQLException {
		ArrayList<Classroom> list = new ArrayList<Classroom>();
		while (resultSet.next()) {
            Classroom classroom = new Classroom(resultSet.getInt("class_number"));
            classroom.setId(resultSet.getInt("id"));
            list.add(classroom);
        }
		return list;
	}

	@Override
	protected void statementForInsert(PreparedStatement statement, Classroom object) throws SQLException {
		statement.setInt(1, object.getNumber());
		statement.setInt(2, universityId);
		
	}

	@Override
	protected void statementForUpdate(PreparedStatement statement, Classroom object) throws SQLException {
		statement.setInt(1, object.getNumber());
		statement.setInt(2, object.getId());
	}

	@Override
	protected void statementForRetrieve(PreparedStatement statement, Integer key) throws SQLException {
		statement.setInt(1, key.intValue());
		
	}

	@Override
	protected void statementForRemove(PreparedStatement statement, Classroom object) throws SQLException{
		statement.setInt(1, object.getId());
		
	}
}

package com.study.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DAOParent<T, PK> implements CRUDInterface<T, PK> {
	protected DaoFactory daoFactory = DaoFactory.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(DAOParent.class);
	protected abstract String getInsertSQL();
	protected abstract String getUpdateSQL();
	protected abstract String getRetrieveSQL();
	protected abstract String getDeleteSQL();
	protected abstract String getAllSQL();
	protected abstract void statementForInsert(PreparedStatement statement, T object) throws SQLException;
	protected abstract void statementForUpdate(PreparedStatement statement, T object) throws SQLException;
	protected abstract void statementForRetrieve(PreparedStatement statement, PK key) throws SQLException;
	protected abstract void statementForRemove(PreparedStatement statement, T object) throws SQLException;
	protected abstract T parseRetrieveSQL(ResultSet resultSet) throws SQLException;
	protected abstract ArrayList<T> parseGetAllSQL(ResultSet resultSet) throws SQLException;
	
	@Override
	public Integer insertInToDB(T object) throws UniversityDBAccessException{
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
					logger.debug("Statement close");
				}
				
				if(con != null) {
					con.close();
					logger.debug("Connection close");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		return generatedId;
	}
	
	@Override
	public boolean update(T object) throws UniversityDBAccessException {
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
				throw new  UniversityDBAccessException("Cannot close connection to database", e);
			}	
		}
		
		return false;	
	}
	
	@Override
	public T getByPK(PK key) throws UniversityDBAccessException{
		String query = getRetrieveSQL();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		T object = null;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query);
			statementForRetrieve(statement, key);
			resultSet = statement.executeQuery();
			object = parseRetrieveSQL(resultSet);
		} catch (SQLException e) {
			throw new  UniversityDBAccessException("Cannot retrieve from database", e);
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
				throw new  UniversityDBAccessException("Cannot close connection to database", e);
			}	
		}
		return object;
	}
	
	@Override
	public boolean removeFromDB(T object) throws UniversityDBAccessException{
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
			throw new  UniversityDBAccessException("cannot delete", e);
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
				throw new  UniversityDBAccessException("Cannot close connection to database", e);
			}	
		}
		
		return false;	
	}
	
	@Override
	public ArrayList<T> getAll() throws UniversityDBAccessException{
		String query = getAllSQL();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ArrayList<T> listT = null;
		
		try {
			con = daoFactory.getConnection();
			statement = con.prepareStatement(query);
			resultSet = statement.executeQuery();
			listT = parseGetAllSQL(resultSet);
		} catch (SQLException e) {
			throw new  UniversityDBAccessException("Cannot get from database", e);
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
				throw new  UniversityDBAccessException("Cannot close connection to database", e);
			}	
		}
		return listT;
	}
}

package com.study.projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoFactory {
	private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
	private static final String user = "postgres";
	private static final String password = "76564532";
	private static final String url = "jdbc:postgresql://localhost:5433/postgres";
	private static final String driver = "org.postgresql.Driver";
	private static final DaoFactory instance = new DaoFactory();
	
	public DaoFactory() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error("PostgreSQL JDBC Driver is not found. Include it in your library path ", e);
		}
	}
	
	public Connection getConnection() throws UniversityDBAccessException {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			logger.debug("You successfully connected to database now");
		} catch (SQLException e) {
			
			throw new UniversityDBAccessException("Connection Failed", e);
		}
		
		return connection;
	}
	
	public static DaoFactory getInstance() {
		return instance;
	}
}

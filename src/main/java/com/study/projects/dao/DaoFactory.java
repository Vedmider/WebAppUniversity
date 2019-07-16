package com.study.projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class DaoFactory {
	private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
	@Value("${connection.user}")
	private String user;
	@Value("${connection.password}")
	private String password;
	@Value("${connection.URL}")
	private String url;
	@Value("${connection.driver}")
	private String driver = "org.postgresql.Driver";
	private static final DaoFactory instance = new DaoFactory();
	
	public DaoFactory() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error(" JDBC Driver is not found. Include it in your library path ", e);
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

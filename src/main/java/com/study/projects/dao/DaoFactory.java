package com.study.projects.dao;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class DaoFactory {
	private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
	private static final DaoFactory instance = new DaoFactory();
	@Value("${connection.user}")
	private String user;
	@Value("${connection.password}")
	private String password;
	@Value("${connection.URL}")
	private String url;
	@Value("${connection.driver}")
	private String driver;
	@Value("${create.table.classrooms}")
	private String createClassrooms;
	@Value("${create.table.groups}")
	private String createGroups;
	@Value("${create.table.lectures}")
	private String createLectures;
	@Value("${create.table.students}")
	private String createStudents;
	@Value("${create.table.teachers}")
	private String createTeachers;
	@Value("${create.table.universities}")
	private String createUniversities;


	public DaoFactory() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error(" JDBC Driver is not found. Include it in your library path ", e);
		}

		if (driver.equals("org.h2.Driver")){
			initiateDatabase();
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

	private void initiateDatabase() {
		Connection con = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try{
			con = DaoFactory.getInstance().getConnection();
			statement = con.createStatement();
			boolean result = statement.execute(createClassrooms);




		} catch (UniversityDBAccessException e){

		} catch (SQLException e){

		}


	}
}

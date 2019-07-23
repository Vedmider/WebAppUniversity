package com.study.projects.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoFactory {
	private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
	private static final DaoFactory instance = new DaoFactory();
	private String user;
	private String password;
	private String url;
	private String driver;

	private String createClassrooms;
	private String createGroups;
	private String createLectures;
	private String createStudents;
	private String createTeachers;
	private String createUniversities;


	public DaoFactory() {
		setConfiguration();

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error(" JDBC Driver is not found. Include it in your library path ", e);
			e.printStackTrace();
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

	void initiateDatabase() {
		Connection con = null;
		Statement statement;
		ResultSet resultSet = null;

		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.error("Failed to initiate Database", e);
		}

		try{

			statement = con.createStatement();
			boolean result = statement.execute(createUniversities);
			System.out.println("creating table is " + result + "SQL query is" + createUniversities);
			//result = statement.execute(createGroups);
			//System.out.println("creating table group is " + result);
			//result = statement.execute(createTeachers);
			//System.out.println("creating table lectures is " + result);
			//result = statement.execute(createStudents);
			//System.out.println("creating students is " + result);
			//result = statement.execute(createClassrooms);
			//System.out.println("creating Universities is " + result);
			//result = statement.execute(createLectures);
			//System.out.println("Creating table teachers is " + result);

		} catch (SQLException e){
		    logger.error("Can not initiate tables in Database");
		}


	}

	private void setConfiguration(){
		try (InputStream input = DaoFactory.class.getClassLoader().getResourceAsStream("application.properties")) {

			Properties prop = new Properties();

			if (input == null) {
				logger.warn("Sorry, unable to find application.properties");
				return;
			}

			//load a properties file from class path, inside static method
			prop.load(input);

			//get the property value and print it out
			this.url = prop.getProperty("connection.URL");
			this.user = prop.getProperty("connection.user");
			this.password = prop.getProperty("connection.password");
			this.driver = prop.getProperty("connection.driver");
			this.createClassrooms = prop.getProperty("create.table.classrooms");
			this.createGroups = prop.getProperty("create.table.groups");
			this.createLectures = prop.getProperty("create.table.lectures");
			this.createStudents = prop.getProperty("create.table.students");
			this.createTeachers = prop.getProperty("create.table.teachers");
			this.createUniversities = prop.getProperty("create.table.universities");
		} catch (IOException ex) {
			logger.error("Could not load properties from file", ex);
		}
	}

	public String getDriver(){
		return driver;
	}

	public String getCreateClassrooms(){
		return createClassrooms;
	}

	public String getCreateGroups(){
		return createGroups;
	}

	public String getCreateLectures() {
		return createLectures;
	}

	public String getCreateStudents() {
		return createStudents;
	}

	public String getCreateTeachers() {
		return createTeachers;
	}

	public String getCreateUniversities(){
		return createUniversities;
	}

}

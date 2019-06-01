package com.study.projects.dao;

public class UniversityDBAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 234234236522243345L;
	private final int errorCode;
	
	public UniversityDBAccessException(int errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public UniversityDBAccessException(String message, Throwable cause, int errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public UniversityDBAccessException(String message, Throwable cause) {
		super(message, cause);
		errorCode = -1;
	}
	
	public UniversityDBAccessException(Throwable cause, int errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	
	public int getCode() {
		return errorCode;
	}
	
}

package com.study.projects.dao;

import java.util.ArrayList;

public interface CRUDInterface<T, PK> {
	
	public Integer insertInToDB(T object) throws UniversityDBAccessException;
	
	public boolean update(T object) throws UniversityDBAccessException;
	
	public T getByPK(PK key) throws UniversityDBAccessException;
	
	public boolean removeFromDB(T object) throws UniversityDBAccessException;
	
	public ArrayList<T> getAll() throws UniversityDBAccessException;
}

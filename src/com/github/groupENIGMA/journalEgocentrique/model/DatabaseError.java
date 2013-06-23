package com.github.groupENIGMA.journalEgocentrique.model;

public class DatabaseError extends IllegalStateException{

	private static final long serialVersionUID = 1L;

	public DatabaseError(){}
	
	public DatabaseError(String msg){
		super(msg);
	}
}

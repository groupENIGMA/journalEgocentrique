package com.github.groupENIGMA.journalEgocentrique.model;

public class ConnectionException extends IllegalStateException{

	private static final long serialVersionUID = 1L;

	public ConnectionException(){}
	
	public ConnectionException(String msg){
		super(msg);
	}
}

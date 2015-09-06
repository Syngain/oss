package com.guanhuodata.admin.exception;

public class LoginException extends Exception {
	private static final long serialVersionUID = -9013904998504734323L;
	public LoginException(String msg){
		super(msg);
	}
	public LoginException(){
		super();
	}
}

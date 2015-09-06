package com.guanhuodata.framework.exception;

public class DuplicateException extends Exception{
    private static final long serialVersionUID = 2558582094800209878L;
    public DuplicateException(){
		super();
	}
	public DuplicateException(String message){
		super(message);
	}
}

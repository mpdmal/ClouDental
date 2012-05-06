package com.mpdmal.cloudental.util.exception;

public class InvalidPasswordException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException(String password, Throwable x) {
		super("Invalid password :"+password,x);
	}

	public InvalidPasswordException(String password) {
		super("Invalid password "+password);
	}
}

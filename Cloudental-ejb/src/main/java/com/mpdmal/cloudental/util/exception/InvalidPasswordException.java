package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class InvalidPasswordException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException(String password, Throwable x) {
		super("Invalid password :"+password,x);
	}

	public InvalidPasswordException(String password) {
		super("Invalid password "+password);
	}
}

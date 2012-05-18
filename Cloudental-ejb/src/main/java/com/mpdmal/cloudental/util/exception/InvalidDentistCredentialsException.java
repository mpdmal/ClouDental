package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class InvalidDentistCredentialsException  extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidDentistCredentialsException(String username, String message, Throwable x) {
		super("INvalid dentist credentials :"+username+"\n"+message,x);
		CloudentUtils.logError("Invalid name, surname or password:"+username+" ["+message+"]");
	}

	public InvalidDentistCredentialsException(String username, String message) {
		super("INvalid dentist credentials :"+username+"\n"+message);
		CloudentUtils.logError("Dentist already exists:"+username+" ["+message+"]");
	}
	
	public InvalidDentistCredentialsException(String username) {
		this(username, "");
	}

	public InvalidDentistCredentialsException(String username, Throwable x) {
		this(username,"", x);
	}
}
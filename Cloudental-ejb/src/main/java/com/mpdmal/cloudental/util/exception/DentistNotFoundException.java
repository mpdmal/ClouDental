package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;

public class DentistNotFoundException  extends Exception {
	private static final long serialVersionUID = 1L;

	public DentistNotFoundException(String username, String message, Throwable x) {
		super("Dentist not found:"+username+"\n"+message,x);
		CloudentUtils.logError("Dentist not found:"+username+" ["+message+"]");
	}

	public DentistNotFoundException(String username, String message) {
		super("Dentist not found:"+username+"\n"+message);
		CloudentUtils.logError("Dentist not found:"+username+" ["+message+"]");
	}

	public DentistNotFoundException(int dentistid, String message) {
		super("Dentist id not found:"+dentistid+"\n"+message);
		CloudentUtils.logError("Dentist id not found:"+dentistid+" ["+message+"]");
	}

	public DentistNotFoundException(int dentistid) {
		this (dentistid, "");
	}

	public DentistNotFoundException(String username) {
		this(username, "");
	}

	public DentistNotFoundException(String username, Throwable x) {
		this(username,"", x);
	}
}
package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;

public class DentistExistsException  extends Exception {
	private static final long serialVersionUID = 1L;

	public DentistExistsException(String dentistid, String message, Throwable x) {
		super("Dentist not found:"+dentistid+"\n"+message,x);
		CloudentUtils.logError("Dentist already exists:"+dentistid+" ["+message+"]");
	}

	public DentistExistsException(String dentistid, String message) {
		super("Dentist not found:"+dentistid+"\n"+message);
		CloudentUtils.logError("Dentist already exists:"+dentistid+" ["+message+"]");
	}
	
	public DentistExistsException(String dentistid) {
		this(dentistid, "");
	}

	public DentistExistsException(String dentistid, Throwable x) {
		this(dentistid,"", x);
	}
}
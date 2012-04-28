package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;

public class DentistNotFoundException  extends Exception {
	private static final long serialVersionUID = 1L;

	public DentistNotFoundException(String dentistid, String message, Throwable x) {
		super("Dentist not found:"+dentistid+"\n"+message,x);
		CloudentUtils.logError("Dentist not found:"+dentistid+" ["+message+"]");
	}

	public DentistNotFoundException(String dentistid, String message) {
		super("Dentist not found:"+dentistid+"\n"+message);
		CloudentUtils.logError("Dentist not found:"+dentistid+" ["+message+"]");
	}
	
	public DentistNotFoundException(String dentistid) {
		this(dentistid, "");
	}

	public DentistNotFoundException(String dentistid, Throwable x) {
		this(dentistid,"", x);
	}
}
package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;

public class PatientExistsException  extends Exception {
	private static final long serialVersionUID = 1L;

	public PatientExistsException(int patientid) {
		this(patientid, "");
	}

	public PatientExistsException(int patientid, String message) {
		super("Patient not found:"+patientid+"\n"+message);
		CloudentUtils.logError("Patient not found:"+patientid+" ["+message+"]");
	}

	public PatientExistsException(int patientid, String message, Throwable x) {
		super("Patient not found:"+patientid+"\n"+message,x);
		CloudentUtils.logError("Patient not found:"+patientid+" ["+message+"]");
	}
	
	public PatientExistsException(int patientid, Throwable x) {
		this(patientid,"", x);
	}


}
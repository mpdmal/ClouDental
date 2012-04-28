package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;

public class PatientAlreadyExistsException  extends Exception {
	private static final long serialVersionUID = 1L;

	public PatientAlreadyExistsException(int patientid) {
		super("Patient not found:"+patientid);
		CloudentUtils.logError("Patient not found:"+patientid);
	}

	public PatientAlreadyExistsException(int patientid, String message) {
		super("Patient not found:"+patientid+"\n"+message);
		CloudentUtils.logError("Patient not found:"+patientid+" ["+message+"]");
	}

	public PatientAlreadyExistsException(int patientid, String message, Throwable x) {
		super("Patient not found:"+patientid+"\n"+message,x);
		CloudentUtils.logError("Patient not found:"+patientid+" ["+message+"]");
	}
	
	public PatientAlreadyExistsException(int patientid, Throwable x) {
		this(patientid,"", x);
	}


}
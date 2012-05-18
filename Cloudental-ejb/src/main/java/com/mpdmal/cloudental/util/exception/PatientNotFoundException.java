package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class PatientNotFoundException  extends CloudentException {
	private static final long serialVersionUID = 1L;

	public PatientNotFoundException(int patientid, String message, Throwable x) {
		super("Patient not found:"+patientid+"\n"+message,x);
		CloudentUtils.logError("Patient not found:"+patientid+" ["+message+"]");
	}

	public PatientNotFoundException(int patientid, String message) {
		super("Patient not found:"+patientid+"\n"+message);
		CloudentUtils.logError("Patient not found:"+patientid+" ["+message+"]");
	}
	
	public PatientNotFoundException(int patientid) {
		this(patientid, "");
	}

	public PatientNotFoundException(int patientid, Throwable x) {
		this(patientid,"", x);
	}
}
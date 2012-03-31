package com.mpdmal.cloudental.util.exception;


public class InvalidPostitAlertException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidPostitAlertException(int invalidtype, Throwable x) {
		super("Postit Alert Type is invalid:"+invalidtype ,x);
	}

	public InvalidPostitAlertException(int invalidtype) {
		super("Postit Alert Type is invalid:"+invalidtype);
	}

}

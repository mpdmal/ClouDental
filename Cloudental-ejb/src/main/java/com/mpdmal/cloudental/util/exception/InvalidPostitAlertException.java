package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.exception.base.CloudentException;


public class InvalidPostitAlertException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidPostitAlertException(int invalidtype, Throwable x) {
		super("Postit Alert Type is invalid:"+invalidtype ,x);
	}

	public InvalidPostitAlertException(int invalidtype) {
		super("Postit Alert Type is invalid:"+invalidtype);
	}

}

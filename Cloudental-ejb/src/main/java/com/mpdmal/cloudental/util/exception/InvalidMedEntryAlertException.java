package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class InvalidMedEntryAlertException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidMedEntryAlertException(int invalidtype, Throwable x) {
		super("Medican history entry AlertType is invalid:"+invalidtype ,x);
	}

	public InvalidMedEntryAlertException(int invalidtype) {
		super("Medical history entry AlertType is invalid:"+invalidtype);
	}

}

package com.mpdmal.cloudental.util.exception;

public class InvalidMedEntryAlertException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidMedEntryAlertException(int invalidtype, Throwable x) {
		super("Medican history entry AlertType is invalid:"+invalidtype ,x);
	}

	public InvalidMedEntryAlertException(int invalidtype) {
		super("Medical history entry AlertType is invalid:"+invalidtype);
	}

}

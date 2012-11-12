package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class InvalidTitleFormatTypeException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidTitleFormatTypeException(int invalidtype, Throwable x) {
		super("Title Format Type is invalid:"+invalidtype ,x);
	}

	public InvalidTitleFormatTypeException(int invalidtype) {
		super("Title Format Type is invalid:"+invalidtype);
	}

}

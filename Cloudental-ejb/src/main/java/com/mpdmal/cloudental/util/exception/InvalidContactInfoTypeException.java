package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class InvalidContactInfoTypeException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidContactInfoTypeException(int invalidtype, Throwable x) {
		super("Contact info type is invalid:"+invalidtype ,x);
	}

	public InvalidContactInfoTypeException(int invalidtype) {
		super("Contact info type is invalid:"+invalidtype);
	}
}

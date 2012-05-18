package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class InvalidAddressTypeException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidAddressTypeException(int invalidtype, Throwable x) {
		super("Address type is invalid:"+invalidtype ,x);
	}

	public InvalidAddressTypeException(int invalidtype) {
		super("Address type is invalid:"+invalidtype);
	}
}

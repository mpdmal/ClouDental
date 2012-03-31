package com.mpdmal.cloudental.util.exception;

public class InvalidAddressTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidAddressTypeException(int invalidtype, Throwable x) {
		super("Address type is invalid:"+invalidtype ,x);
	}

	public InvalidAddressTypeException(int invalidtype) {
		super("Address type is invalid:"+invalidtype);
	}
}

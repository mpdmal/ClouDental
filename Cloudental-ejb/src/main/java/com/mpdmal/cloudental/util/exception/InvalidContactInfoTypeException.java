package com.mpdmal.cloudental.util.exception;

public class InvalidContactInfoTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidContactInfoTypeException(int invalidtype, Throwable x) {
		super("Contact info type is invalid:"+invalidtype ,x);
	}

	public InvalidContactInfoTypeException(int invalidtype) {
		super("Contact info type is invalid:"+invalidtype);
	}
}

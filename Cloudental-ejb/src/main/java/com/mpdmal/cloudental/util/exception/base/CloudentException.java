package com.mpdmal.cloudental.util.exception.base;

public class CloudentException extends Exception {
	private static final long serialVersionUID = 1L;

	public CloudentException() {
		super();
	}
	
	public CloudentException(String message) {
		super(message);
	}

	public CloudentException(Throwable t) {
		super(t);
	}

	public CloudentException(String message, Throwable t) {
		super(message,t);
	}
}

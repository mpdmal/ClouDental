package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.exception.base.CloudentException;


public class InvalidMedIntakeRouteException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public InvalidMedIntakeRouteException(int invalidtype, Throwable x) {
		super("Medicine intake route is invalid:"+invalidtype ,x);
	}

	public InvalidMedIntakeRouteException(int invalidtype) {
		super("Medicine intake route is invalid:"+invalidtype);
	}

}

package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class ValidationException extends CloudentException {
	private static final long serialVersionUID = 1L;

	public ValidationException(String msg, Throwable t) {
		super(msg, t);
		CloudentUtils.logError("Entity Constraint violated:"+msg);
	}
}

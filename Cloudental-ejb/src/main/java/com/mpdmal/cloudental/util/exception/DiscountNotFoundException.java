package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class DiscountNotFoundException  extends CloudentException {
	private static final long serialVersionUID = 1L;

	public DiscountNotFoundException(String title, String message, Throwable x) {
		super("Discount not found:"+title+"\n"+message,x);
		CloudentUtils.logError("Discount not found:"+title+" ["+message+"]");
	}

	public DiscountNotFoundException(String title, String message) {
		super("Discount not found:"+title+"\n"+message);
		CloudentUtils.logError("Discount not found:"+title+" ["+message+"]");
	}

	public DiscountNotFoundException(int id, String message) {
		super("Discount not found:"+id+"\n"+message);
		CloudentUtils.logError("Discount not found:"+id+" ["+message+"]");
	}

	public DiscountNotFoundException(int id) {
		this (id, "");
	}

	public DiscountNotFoundException(String title) {
		this(title, "");
	}

	public DiscountNotFoundException(String title, Throwable x) {
		this(title,"", x);
	}
}
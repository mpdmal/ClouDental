package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;

public class PricelistItemNotFoundException  extends Exception {
	private static final long serialVersionUID = 1L;

	public PricelistItemNotFoundException(String title, String message, Throwable x) {
		super("Pricelist Item not found:"+title+"\n"+message,x);
		CloudentUtils.logError("Pricelist Item not found:"+title+" ["+message+"]");
	}

	public PricelistItemNotFoundException(String title, String message) {
		super("Pricelist Item not found:"+title+"\n"+message);
		CloudentUtils.logError("Pricelist Item not found:"+title+" ["+message+"]");
	}

	public PricelistItemNotFoundException(int id, String message) {
		super("Pricelist Item id not found:"+id+"\n"+message);
		CloudentUtils.logError("Pricelist Item id not found:"+id+" ["+message+"]");
	}

	public PricelistItemNotFoundException(int id) {
		this (id, "");
	}

	public PricelistItemNotFoundException(String title) {
		this(title, "");
	}

	public PricelistItemNotFoundException(String title, Throwable x) {
		this(title,"", x);
	}
}
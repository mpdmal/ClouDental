package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class VisitNotFoundException  extends CloudentException {
	private static final long serialVersionUID = 1L;

	public VisitNotFoundException(int visitid) {
		super("Visit not found:"+visitid);
		CloudentUtils.logError("Visit not found:"+visitid);
	}

	public VisitNotFoundException(int visitid, String message) {
		super("Visit not found:"+visitid+"\n"+message);
		CloudentUtils.logError("Visit not found:"+visitid+" ["+message+"]");
	}

	public VisitNotFoundException(int visitid, String message, Throwable x) {
		super("Visit not found:"+visitid+"\n"+message,x);
		CloudentUtils.logError("Visit not found:"+visitid+" ["+message+"]");
	}
	
	public VisitNotFoundException(int visitid, Throwable x) {
		this(visitid,"", x);
	}


}
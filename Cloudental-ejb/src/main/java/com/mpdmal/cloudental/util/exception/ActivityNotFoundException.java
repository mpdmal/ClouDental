package com.mpdmal.cloudental.util.exception;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class ActivityNotFoundException  extends CloudentException {
	private static final long serialVersionUID = 1L;

	public ActivityNotFoundException(int activityid) {
		super("Activity not found:"+activityid+"\n");
		CloudentUtils.logError("Activity not found:"+activityid);
	}

	public ActivityNotFoundException(int activityid, String message) {
		super("Activity not found:"+activityid+"\n"+message);
		CloudentUtils.logError("Activity not found:"+activityid+" ["+message+"]");
	}

	public ActivityNotFoundException(int activityid, String message, Throwable x) {
		super("Activity not found:"+activityid+"\n"+message,x);
		CloudentUtils.logError("Activity not found:"+activityid+" ["+message+"]");
	}
	
	public ActivityNotFoundException(int activityid, Throwable x) {
		this(activityid,"", x);
	}


}
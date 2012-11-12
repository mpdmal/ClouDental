package com.mpdmal.cloudental.web.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class CloudentWebUtils {
	public static final String DEFAULT_PRIMEFACES_THEME = "cupertino";
	public static void showJSFMessage(String title, String msg, Severity sev) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage( sev, title, msg));
	}

	public static void showJSFErrorMessage(String title, String msg) {
		showJSFMessage(title, msg, FacesMessage.SEVERITY_ERROR);
	}
	public static void showJSFErrorMessage(String msg) {
		showJSFErrorMessage("", msg);
	}
	
	
	public static void showJSFInfoMessage(String title, String msg) {
		showJSFMessage(title, msg, FacesMessage.SEVERITY_INFO);
	}
	public static void showJSFInfoMessage(String msg) {
		showJSFInfoMessage("", msg);
	}

	
	public static void showJSFFatalMessage(String title, String msg) {
		showJSFMessage(title, msg, FacesMessage.SEVERITY_FATAL);
	}
	public static void showJSFFatalMessage(String msg) {
		showJSFFatalMessage("", msg);
	}

	
	public static void showJSFWarnMessage(String title, String msg) {
		showJSFMessage(title, msg, FacesMessage.SEVERITY_WARN);
	}
	public static void showJSFWarnlMessage(String msg) {
		showJSFWarnMessage("", msg);
	}
}

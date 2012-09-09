package com.mpdmal.cloudental.util.exception.base;

public class CloudentException extends Exception {
	private static final long serialVersionUID = 1L;
	public static final String EXCEPTION_NODE = "<exception>";
	public static final String EXCEPTION_ENDNODE = "</exception>";
	
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
	
	public String getXML() {
		StringBuffer ans = new StringBuffer(EXCEPTION_NODE+EXCEPTION_ENDNODE);
		ans.insert(ans.indexOf(EXCEPTION_ENDNODE), getMessage());
		return ans.toString();
	}
}

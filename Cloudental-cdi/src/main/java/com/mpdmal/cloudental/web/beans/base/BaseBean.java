package com.mpdmal.cloudental.web.beans.base;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.mpdmal.cloudental.util.CloudentUtils;

@Named
public class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public BaseBean() {
		CloudentUtils.logServicecall("created (from base):"  +getClass());
	}
	@PostConstruct
	public void init() {
		CloudentUtils.logServicecall("instantiated (from base):"  +getClass());
	}

	@AroundInvoke
	public Object testicept(InvocationContext t) throws Exception {
		CloudentUtils.logMethodInfo(t.getMethod(), t.getParameters());
		return t.proceed();
	}
	
}

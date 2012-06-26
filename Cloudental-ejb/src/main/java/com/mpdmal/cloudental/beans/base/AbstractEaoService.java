package com.mpdmal.cloudental.beans.base;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;
import com.mpdmal.cloudental.util.exception.VisitNotFoundException;

@Named
public class AbstractEaoService implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	protected EaoManager emgr;
	
	//AOP interceptor to all business logic methods .. and those of extending classes ...
    @AroundInvoke
    public Object CloudentServiceAdvice(InvocationContext ic) throws Exception {
    	logMethodInfo(ic.getMethod(), ic.getParameters());
    	logContextData(ic.getContextData());
    	return ic.proceed();
    }
    //private
    private void logMethodInfo(Method m, Object[] prms) {
    	StringBuilder sb = new StringBuilder("\nbean service fired:"+m.getName());
    	if (prms == null) {
    		sb.append("  [no args]");
    		CloudentUtils.logServicecall(sb.toString());
    		return;
    	}
    	
    	sb.append("  [ ");
    	for (Object o : prms) {
    		if (o!=null)
			  sb.append(o.toString()).append(", ");
		}
    	sb.delete(sb.length()-2, sb.length());
    	sb.append("]");
    	CloudentUtils.logServicecall(sb.toString());
    }

    private void logContextData(Map<String, Object> data) {
    	Set<String> keys = data.keySet();
    	StringBuilder sb = new StringBuilder();
    	sb.append("\nContext Data ------\n");
    	for (String key : keys) {
			Object val = data.get(key);
			sb.append(key).append(":").append(val.toString()).append("\n");
		}
    	sb.append("End Context Data ------");
		CloudentUtils.logServicecall(sb.toString());
    }

    //inherited to children
    public Dentist findDentist(int id) throws DentistNotFoundException {
    	Dentist d = emgr.findOrFail(Dentist.class, id);
		if (d == null) 
			throw new DentistNotFoundException(id);
		return d;     
    }

    public Dentist findDentistByUsername(String username) throws DentistNotFoundException {
    	Query q = emgr.getEM().createQuery("select d from Dentist d where d.username= :username")
    			.setParameter("username", username);
    	Dentist d = (Dentist)emgr.executeSingleObjectQuery(q);
    	 
		if (d == null) 
			throw new DentistNotFoundException(username);
		return d;     
    }

    public PricelistItem findPricable(int id) throws PricelistItemNotFoundException {
    	PricelistItem item = emgr.findOrFail(PricelistItem.class, id);
    	if (item == null)
    		throw new PricelistItemNotFoundException(id);
    	return item;
    }
    public Discount findDiscount(int id) throws DiscountNotFoundException {
    	Discount d = emgr.findOrFail(Discount.class, id);
    	if (d == null)
    		throw new DiscountNotFoundException(id);
    	return d;
    }
    public Patient findPatient(int id) throws PatientNotFoundException {
    	Patient p = emgr.findOrFail(Patient.class, id);
    	if (p == null)
    		throw new PatientNotFoundException(id);
    	return p;
    }
    
    public Activity findActivity(int id) throws ActivityNotFoundException {
    	Activity ac = emgr.findOrFail(Activity.class, id);
    	if (ac == null)
    		throw new ActivityNotFoundException(id);
    	return ac;
    }

    public Visit findVisit(int id) throws VisitNotFoundException {
    	Visit v = emgr.findOrFail(Visit.class, id);
    	if (v == null)
    		throw new VisitNotFoundException(id);
    	return v;
    }

}

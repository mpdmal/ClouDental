package com.mpdmal.cloudental.beans.base;

import java.io.Serializable;

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
    	CloudentUtils.logMethodInfo(ic.getMethod(), ic.getParameters());
    	CloudentUtils.logContextData(ic.getContextData());
    	return ic.proceed();
    }

    //inherited to children
    public Dentist findDentist(int id) throws DentistNotFoundException {
    	Dentist d = emgr.findOrFail(Dentist.class, id);
		if (d == null) 
			throw new DentistNotFoundException(id);
		return d;     
    }

    public Dentist findDentistByUsername(String username) {
    	Query q = emgr.getEM().createQuery("select d from Dentist d where d.username= :username")
    			.setParameter("username", username);
    	return (Dentist)emgr.executeSingleObjectQuery(q);
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

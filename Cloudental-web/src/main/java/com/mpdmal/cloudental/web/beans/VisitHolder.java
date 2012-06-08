
package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Visit;


public class VisitHolder implements Serializable{

	private static final long serialVersionUID = 1L;
	private Vector<Visit> visitList;
	
	

	public Vector<Visit> getVisitList() {
		return visitList;
	}

	public void setVisitList(Vector<Visit> visitList) {
		this.visitList = visitList;
	}

	public VisitHolder() {
		super();
		System.out.println("VisitHolder costructor"+this.hashCode());
	}

	@PostConstruct
	public void initialize() {
		//System.out.println("ActivityManagementBean PostConstruct");

		FacesContext context = FacesContext.getCurrentInstance();
		VisitManagementBean visitManagementBean = (VisitManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{visitManagementBean}", VisitManagementBean.class);
		if(visitManagementBean!=null)
			visitList = visitManagementBean.loadPatientVisits();
	}


	public void loadActivityVisits(Activity activity){
		FacesContext context = FacesContext.getCurrentInstance();
		VisitManagementBean visitManagementBean = (VisitManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{visitManagementBean}", VisitManagementBean.class);
		visitManagementBean.setActivity(activity);
		visitList = visitManagementBean.loadActivityVisits();
	}
	
	public void loadPatientVisits(Patient p ){
		FacesContext context = FacesContext.getCurrentInstance();
		VisitManagementBean visitManagementBean = (VisitManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{visitManagementBean}", VisitManagementBean.class);
		visitManagementBean.setSelectedPatient(p);
		visitList = visitManagementBean.loadPatientVisits();
		
	}
	
	




}

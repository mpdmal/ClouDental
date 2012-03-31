package com.mpdmal.cloudental.web.listeners;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.web.beans.DentistManagementBean;

public class OnDeleteDentistActionListener implements ActionListener {


	@Override
	public void processAction(ActionEvent event) throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		Dentist d = (Dentist) app.evaluateExpressionGet(context, "#{dentist}",	Dentist.class);
		System.out.println("OnDeleteDentistActionListener");
		System.out.println(d.getUsername());
		DentistManagementBean dmb = (DentistManagementBean) app.evaluateExpressionGet(context, "#{dentistManagementBean}",	DentistManagementBean.class);
		dmb.setUsername(d.getUsername());
		dmb.setName(d.getName());
		dmb.setPassword(d.getPassword());
		dmb.setSurname(d.getSurname());
		
		
		dmb.deleteDentist();
		
	}

}

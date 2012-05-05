package com.mpdmal.cloudental.web.listeners;

import java.util.Vector;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.web.beans.DentistManagementBean;
import com.mpdmal.cloudental.web.beans.DiscountManagementBean;

public class OnDeleteDiscountActionListener implements ActionListener {


	@Override
	public void processAction(ActionEvent event) throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
	}

}

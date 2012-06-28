package com.mpdmal.cloudental.web.listeners;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class OnDeleteDiscountActionListener implements ActionListener {


	@Override
	public void processAction(ActionEvent event) throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		System.out.println(app.toString());
	}

}

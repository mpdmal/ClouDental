package com.mpdmal.cloudental.web.beans;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.LoginBean;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidPasswordException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;
import com.mpdmal.cloudental.web.controllers.Office;

@Named("loginSvc")
@RequestScoped
public class LoginServiceBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//CDI BEANS
	@Inject
	Office office;
	@Inject
	LoginBean loginBean;
	
	//MODEL
	private String name = "demo", password = "demo";

	//GETTERS/SETTERS
	public String getName() {	return name;	}
	public String getPassword() {	return password;	}

	public void setName(String name) {	this.name = name;	}
	public void setPassword(String password) {	this.password = password;	}

	//INTERFACE
	public String login() {
		Dentist d = null;
		try {
			d = loginBean.doLogin(name, password);
		} catch (DentistNotFoundException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return null;
		} catch (InvalidPasswordException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			CloudentUtils.logError(e.getMessage());
			return null;
		}
		office.setOwnerAndPopulate(d.getId());
		return "office";
	}
}

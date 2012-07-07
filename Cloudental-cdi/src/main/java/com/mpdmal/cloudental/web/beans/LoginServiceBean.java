package com.mpdmal.cloudental.web.beans;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.mpdmal.cloudental.beans.LoginBean;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
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
	public void login(ActionEvent actionEvent) {  
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg = null;  
        boolean loggedIn = false;  

		Dentist d = null;
		try {
			d = loginBean.doLogin(name, password);
		} catch (CloudentException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", e.getMessage());
            loggedIn = false;  
		} catch (Exception e) {
			e.printStackTrace();
			CloudentUtils.logError(e.getMessage());
            loggedIn = false;  
		}
		office.setOwnerAndPopulate(d.getId());
        loggedIn = true;  
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome to CLoud M ", d.getUsername());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        context.addCallbackParam("loggedIn", loggedIn);  
    }  
}

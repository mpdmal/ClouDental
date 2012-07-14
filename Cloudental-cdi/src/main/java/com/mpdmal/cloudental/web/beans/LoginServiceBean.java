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

@Named("loginService")
@RequestScoped
public class LoginServiceBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//CDI BEANS
	@Inject
	LoginBean loginBean;
	@Inject
	OfficeReceptionBean sess;
	
	public LoginServiceBean() {
		super();
		_baseName = "Login Service";
	}
	//MODEL 
	private String name = "", password = "";

	//GETTERS/SETTERS
	public String getName() {	return name;	}
	public String getPassword() {	return password;	}

	public void setName(String name) {	this.name = name;	}
	public void setPassword(String password) {	this.password = password;	}

	//INTERFACE
	public String login() {  
        FacesMessage msg = null;  

		Dentist d = null;
		try {
			d = loginBean.doLogin(name, password);
		} catch (CloudentException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);  
            return null;
		} catch (Exception e) {
			e.printStackTrace();
			CloudentUtils.logError(e.getMessage());
			return null;
		}
		sess.setUserID(d.getId());
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome to Cloud.M ", d.getUsername());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        return "reception";
    }  
}

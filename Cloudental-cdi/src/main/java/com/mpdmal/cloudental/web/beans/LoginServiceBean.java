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
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

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
		Dentist d = null;
		try {
			d = loginBean.doLogin(name, password);
		} catch (CloudentException e) {
            CloudentWebUtils.showJSFErrorMessage("", e.getMessage());
            return null;
		} catch (Exception e) {
			e.printStackTrace();
			CloudentUtils.logError(e.getMessage());
			return null;
		}
		sess.setUserID(d.getId());
		CloudentWebUtils.showJSFInfoMessage("Welcome to Cloud.M", d.getUsername());
        return "reception";
    }  
}

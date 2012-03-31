package com.mpdmal.cloudental.web.beans;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.LoginBean;
import com.mpdmal.cloudental.entities.Dentist;

public class LoginRequest {
	
	@EJB
	LoginBean loginBean;
	
	
	private String username="demo";
	private String password="demo";


	public String getUsername() {
		System.out.println("getUsername: "+username);
		return username;
	}
	public void setUsername(String username){
		System.out.println("setUsername: "+username);
		this.username = username;
	}
	public String getPassword() {
		System.out.println("setPassword: ");
		return password;
	}
	public void setPassword(String password) {
		System.out.println("getPassword: ");
		this.password = password;
	}
	public String login() {
		System.out.println("login");
		Dentist d = loginBean.doLogin(username, password);
		
		
		if (d!=null) {
			UserHolder userHolder = new UserHolder();
			
			userHolder.setCurrentUser(d);
			
			//set session bean
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userHolder",userHolder); 
			
			
			return "loggedIn";
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Login failed", null));
			return null;}
	}




}

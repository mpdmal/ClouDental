package com.mpdmal.cloudental.web.beans;

import java.util.List;

//import javax.annotation.ManagedBean;
import javax.ejb.EJB;


import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.entities.Dentist;

public class DentistManagementBean extends Dentist {



	private static final long serialVersionUID = 1L;
	@EJB
	DentistBean dbn;
	
	public List<Dentist> getDentists() {
		System.out.println("getDentists(): called" );
		return dbn.getDentists();
	}




	public String createDentist(){
		System.out.println("create: "+getUsername());
		dbn.createDentist(getUsername() , getPassword(), getSurname() , getName());
		return null;
	}

	public String deleteDentist() {
		System.out.println("delete: "+getUsername());

		dbn.deleteDentist(getUsername());
		return null;
	}

	public String updateDentist() {
		System.out.println("update: "+getUsername());
		dbn.updateDentist(this);
		return null;
	}


	public void setCurrentDentist(Dentist currentDentist) {
		setUsername(currentDentist.getUsername());
		setPassword(currentDentist.getPassword());
		setName(currentDentist.getName());
		setSurname(currentDentist.getSurname());
	}




}

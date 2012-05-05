package com.mpdmal.cloudental.web.beans;

import java.util.List;

//import javax.annotation.ManagedBean;
import javax.ejb.EJB;


import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidDentistCredentialsException;

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
		Dentist d = null;
		try {
			try {
				d = dbn.createDentist(getName() ,getSurname() , getUsername(), getPassword());
			} catch (InvalidDentistCredentialsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (DentistExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (d == null)
			return null;
		return d.getUsername();
	}

	public String deleteDentist() {
		System.out.println("delete: "+getUsername());

		try {
			dbn.deleteDentist(getUsername());
		} catch (DentistNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String updateDentist() {
		System.out.println("update: "+getUsername());
		try {
			dbn.updateDentist(this);
		} catch (DentistNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public void setCurrentDentist(Dentist currentDentist) {
		setUsername(currentDentist.getUsername());
		setPassword(currentDentist.getPassword());
		setName(currentDentist.getName());
		setSurname(currentDentist.getSurname());
	}




}

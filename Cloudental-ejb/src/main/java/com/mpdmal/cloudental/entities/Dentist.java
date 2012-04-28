package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.PatientExistsException;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Dentist extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.EAGER)
	private Set<Patient> patients;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.EAGER)
	private Set<Postit> notes;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="dentist")
	private Set<PricelistItem> priceables;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="dentist")
	private Set<Discount> discounts;

    @NotNull
    private String name;
    @NotNull
	private String surname;
    @NotNull
	private String password;

    public Dentist() {}

	public String getSurname() {	return this.surname;	}
	public String getUsername() {	return this.username;	}
	public String getPassword() {	return this.password;	}
	public String getName() {	return this.name;	}
	public Set<Patient> getPatients() {	return this.patients;	}
	public Set<Postit> getNotes() {	return this.notes;	}
	public Set<PricelistItem> getPriceList() {	return priceables;	}
	public Set<Discount> getDiscounts() {	return discounts;	}
	public void setName(String name){	this.name = name;	}
	public void setSurname(String name){	this.surname = name;	}
	public void setUsername(String name){	this.username = name;	}
	public void setPassword(String password){	this.password = password;	}
	public void setPricelist(Set<PricelistItem> pc) {	 	
		if (priceables!= null)
			priceables.clear();
		for (PricelistItem item : priceables) {
			addPricelistItem(item);
		}
	}
	public void addPricelistItem(PricelistItem item) {	
		if (priceables == null)
			priceables = new HashSet<PricelistItem>();
		
		item.setDentist(this);
		priceables.add(item);
	}

	public void setDiscounts(Set<Discount> ds) {	 
		if (discounts!= null)
			discounts.clear();
		for (Discount discount : discounts) {
			addDiscount(discount);
		}
	}
	public void addDiscount(Discount ds) {	
		if (discounts == null)
			discounts = new HashSet<Discount>();
		
		ds.setDentist(this);
		discounts.add(ds);
	}
	public void setPatients(Set<Patient> patients) throws PatientExistsException {
		if (this.patients != null)
			this.patients.clear();
		
		for (Patient patient : patients) {
			addPatient(patient);
		}
	}
	public void addPatient(Patient p) throws PatientExistsException {
		if (patients == null)
			patients = new HashSet<Patient>();
		for (Patient patient : patients) {
			if (patient.getName().equals(p.getName())
					&& patient.getSurname().equals(p.getSurname())) 
				throw new PatientExistsException(p.getId(), "Cannot add patient with same name and surname:"+p.getName()+"|"+p.getSurname());
		}
		p.setDentist(this);
		patients.add(p);
	}
	
	public void setNotes(Set<Postit> notes) {	
		if (notes != null)
			notes.clear();
		for (Postit postit : notes) {
			addNote(postit);
		}
	}
	public void addNote(Postit note) {	
		if (notes == null)
			notes = new HashSet<Postit>();
		
		note.setDentist(this);
		notes.add(note);
	}

	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<dentist></dentist>");
		ans.insert(ans.indexOf("</dentist"), "<name>"+name+"</name>");
		ans.insert(ans.indexOf("</dentist"), "<surname>"+surname+"</surname>");
		ans.insert(ans.indexOf("</dentist"), "<username>"+username+"</username>");
		ans.insert(ans.indexOf("</dentist"), "<password>"+password+"</password>");
		
		ans.insert(ans.indexOf("</dentist"), "<patientlist>");
		for (Patient patient : patients) {
			ans.insert(ans.indexOf("</dentist"), patient.getXML());
		}
		ans.insert(ans.indexOf("</dentist"), "</patientlist>");
		
		ans.insert(ans.indexOf("</dentist"), "<pinboard>");
		for (Postit note: notes) {
			ans.insert(ans.indexOf("</dentist"), note.getXML());
		}
		ans.insert(ans.indexOf("</dentist"), "</pinboard>");
		
		ans.insert(ans.indexOf("</dentist"), "<pricelist>");
		for (PricelistItem pbl : priceables) {
			ans.insert(ans.indexOf("</dentist"), pbl.getXML());
		}
		ans.insert(ans.indexOf("</dentist"), "</pricelist>");

		ans.insert(ans.indexOf("</dentist"), "<discounts>");
		for (Discount ds: discounts) {
			ans.insert(ans.indexOf("</discount"), ds.getXML());
		}
		ans.insert(ans.indexOf("</dentist"), "</discounts>");

		return ans.toString();
	}
}
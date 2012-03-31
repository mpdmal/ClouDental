package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.mpdmal.cloudental.util.CloudentUtils;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Dentist extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Set<Patient> patients;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.EAGER)
	private Set<Postit> notes;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="dentist")
	private Set<Pricable> pricables;
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
	public Set<Pricable> getPriceList() {	return pricables;	}
	
	public void setName(String name){	this.name = name;	}
	public void setSurname(String name){	this.surname = name;	}
	public void setUsername(String name){	this.username = name;	}
	public void setPassword(String password){	this.password = password;	}
	public void setPatients(Set<Patient> patients) {
		if (patients == null)
			patients = new HashSet<Patient>();
		else
			patients.clear();
		for (Patient patient : patients) {
			addPatient(patient);
		}
	}
	public void addPatient(Patient p) {
		if (patients == null)
			patients = new HashSet<Patient>();
		for (Patient patient : patients) {
			if (patient.getName().equals(p.getName())
					&& patient.getSurname().equals(p.getSurname())) {
				CloudentUtils.logWarning("Cannot add patient with same name and surname:"+p.getName()+"|"+p.getSurname());
				return;				
			}
		}
		p.setDentist(this);
		patients.add(p);
	}
	public void setNotes(Set<Postit> notes) {	
		if (notes == null)
			notes = new HashSet<Postit>();
		else
			notes.clear();
		for (Postit postit : notes) {
			postNote(postit);
		}
	}
	public void postNote(Postit note) {	
		if (notes == null)
			notes = new HashSet<Postit>();
		
		note.setDentist(this);
		notes.add(note);
	}
	public void setPricelist(Set<Pricable> pc) {	pricables = pc;	}
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
		for (Pricable pbl : pricables) {
			ans.insert(ans.indexOf("</dentist"), pbl.getXML());
		}
		ans.insert(ans.indexOf("</dentist"), "</pricelist>");
		
		return ans.toString();
	}
}
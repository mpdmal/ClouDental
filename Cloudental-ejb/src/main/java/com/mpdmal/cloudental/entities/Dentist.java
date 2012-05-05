package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="dentist")
public class Dentist extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable=false, length=80)
	private String name;
	@Column(nullable=false, length=16)
	private String password;
	@Column(nullable=false, length=80)
	private String surname;
	@Column(nullable=false, length=16)
	private String username;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Collection<Discount> discounts;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Collection<Postit> postits;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Collection<PricelistItem> priceables;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.EAGER)
	private Collection<Patient> patients;


   public Dentist() {}

   public Integer getId() {	return this.id;	}
	public String getSurname() {	return this.surname;	}
	public String getUsername() {	return this.username;	}
	public String getPassword() {	return this.password;	}
	public String getName() {	return this.name;	}
	public void setId(Integer id) {	this.id = id;	}
	public void setName(String name){	this.name = name;	}
	public void setSurname(String name){	this.surname = name;	}
	public void setUsername(String name){	this.username = name;	}
	public void setPassword(String password){	this.password = password;	}

	
	//PATIENTS
	public Collection<Patient> getPatientList() {	return patients;	}
	public void setPatients(Collection<Patient> patients) {
		if (patients != null)
			patients.clear();
		for (Patient patient : patients) {
			addPatient(patient);
		}
	}
	public void addPatient(Patient p) {
		if (patients == null)
			patients = new ArrayList<Patient>();
		
		patients.add(p);
	}

	public void removePatient(Patient p) {
		if (patients.contains(p))
			patients.remove(p);
	}
	//PRICABLES
	public Collection<PricelistItem> getPriceList() {	return priceables;	}
	public void setPricelist(final Collection<PricelistItem> pc) {	 	
		if (priceables!= null)
			priceables.clear();
		for (PricelistItem item : priceables) {
			addPricelistItem(item);
		}
	}
	public void addPricelistItem(final PricelistItem item) {	
		if (priceables == null)
			priceables = new ArrayList<PricelistItem>();
		
		priceables.add(item);
	}
	public void removePricelistItem(final PricelistItem item) {
		if (priceables.contains(item))
			priceables.remove(item);
	}

	//DISCOUNTS
	public Collection<Discount> getDiscounts() {	return discounts;	}
	public void setDiscounts(Collection<Discount> ds) {	 
		if (discounts!= null)
			discounts.clear();
		for (Discount discount : discounts) {
			addDiscount(discount);
		}
	}
	public void addDiscount(Discount ds) {	
		if (discounts == null)
			discounts = new ArrayList<Discount>();
		
		ds.setDentist(this);
		discounts.add(ds);
	}
	public void removeDiscount(Discount d) {
		if (discounts.contains(d))
			discounts.remove(d);
	}
	
	//POST-IT
	public Collection<Postit> getNotes() {	return this.postits;	}
	public void setNotes(Collection<Postit> notes) {	
		if (this.postits != null)
			this.postits.clear();
		for (Postit postit : notes) {
			addNote(postit);
		}
	}
	public void addNote(Postit note) {	
		if (postits == null)
			postits = new ArrayList<Postit>();
		
		note.setDentist(this);
		getNotes().add(note);
	}

	public void removeNote (Postit note) {
		if (getNotes().contains(note))
			postits.remove(note);
	}
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<dentist></dentist>");
		ans.insert(ans.indexOf("</dentist"), "<name>"+name+"</name>");
		ans.insert(ans.indexOf("</dentist"), "<surname>"+surname+"</surname>");
		ans.insert(ans.indexOf("</dentist"), "<username>"+username+"</username>");
		ans.insert(ans.indexOf("</dentist"), "<password>"+password+"</password>");
		
		ans.insert(ans.indexOf("</dentist"), "<pinboard>");
		for (Postit note: postits) {
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
		ans.insert(ans.indexOf("</dentist"), "<patientlist>");
		for (Patient patient : patients) {
			ans.insert(ans.indexOf("</dentist"), patient.getXML());
		}
		ans.insert(ans.indexOf("</dentist"), "</patientlist>");

		return ans.toString();
	}
}
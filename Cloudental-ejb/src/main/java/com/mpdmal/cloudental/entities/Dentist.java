package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="dentist")
public class Dentist extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//Note, there is no point in carrying the dentist prefs around
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true)
	private Integer id;

	@NotNull
	@NotEmpty
	@Column(length=80)
	private String name;
	@NotNull
	@NotEmpty
	@Column(length=16)
	private String password;
	@NotNull
	@NotEmpty
	@Column(length=80)
	private String surname;
	@NotNull
	@NotEmpty
	@Column(unique=true, length=16)
	private String username;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Collection<Discount> discounts;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Collection<Postit> postits;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Collection<PricelistItem> priceables;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.EAGER)
	private Collection<Patient> patients;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dentist", fetch=FetchType.LAZY)
	private Collection<Prescription> prescriptions;

	public Dentist() {}

	@Override
	public String getUIFriendlyString() {
		return getName()+" "+getSurname()+" ("+getUsername()+")";
	}
   	public Integer getId() {	return this.id;	}
	public String getSurname() {	return this.surname;	}
	public String getUsername() {	return this.username;	}
	public String getPassword() {	return this.password;	}
	public String getName() {	return this.name;	}
	public Collection<Prescription> getPrescriptions() {	return this.prescriptions;	}
	public void setPrescriptions(Collection<Prescription> prescriptions) {	this.prescriptions = prescriptions;	}
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
	
	
	public static final String DENTIST_NODE = "<dentist>";
	public static final String DENTIST_ENDNODE = "</dentist>";
	public static final String DENTIST_NAMENODE = "<name>";
	public static final String DENTIST_NAMEENDNODE = "</name>";
	public static final String DENTIST_SURNAMENODE = "<surname>";
	public static final String DENTIST_SURNAMEENDNODE = "</surname>";
	public static final String DENTIST_USERNAMENODE = "<username>";
	public static final String DENTIST_USERNAMEENDNODE = "</username>";
	public static final String DENTIST_PASSWORDNODE = "<password>";
	public static final String DENTIST_PASSWORDENDNODE = "</password>";
	public static final String DENTIST_IDNODE = "<id>";
	public static final String DENTIST_IDENDNODE = "</id>";

	public String getBASICXML() {
		StringBuilder ans= new StringBuilder(DENTIST_NODE+DENTIST_ENDNODE);
		ans.insert(ans.indexOf(DENTIST_ENDNODE), DENTIST_IDNODE+getId()+DENTIST_IDENDNODE);
		ans.insert(ans.indexOf(DENTIST_ENDNODE), DENTIST_NAMENODE+name+DENTIST_NAMEENDNODE);
		ans.insert(ans.indexOf(DENTIST_ENDNODE), DENTIST_SURNAMENODE+surname+DENTIST_SURNAMEENDNODE);
		ans.insert(ans.indexOf(DENTIST_ENDNODE), DENTIST_USERNAMENODE+username+DENTIST_USERNAMEENDNODE);
		ans.insert(ans.indexOf(DENTIST_ENDNODE), DENTIST_PASSWORDNODE+password+DENTIST_PASSWORDENDNODE);
		return ans.toString();
	}
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder(getBASICXML());
		ans.insert(ans.indexOf(DENTIST_ENDNODE), "<pinboard>");
		if (postits != null)
		for (Postit note: postits) {
			ans.insert(ans.indexOf(DENTIST_ENDNODE), note.getXML());
		}
		ans.insert(ans.indexOf(DENTIST_ENDNODE), "</pinboard>");
		
		ans.insert(ans.indexOf(DENTIST_ENDNODE), "<pricelist>");
		if (priceables != null)
			for (PricelistItem pbl : priceables) 
				ans.insert(ans.indexOf(DENTIST_ENDNODE), pbl.getXML());
		ans.insert(ans.indexOf(DENTIST_ENDNODE), "</pricelist>");

		ans.insert(ans.indexOf(DENTIST_ENDNODE), "<discounts>");
		if (discounts!= null)
			for (Discount ds: discounts) 
				ans.insert(ans.indexOf(DENTIST_ENDNODE), ds.getXML());
		ans.insert(ans.indexOf(DENTIST_ENDNODE), "</discounts>");
		
		ans.insert(ans.indexOf(DENTIST_ENDNODE), "<patientlist>");
		if (patients != null) 
			for (Patient patient : patients) 
				ans.insert(ans.indexOf(DENTIST_ENDNODE), patient.getXML());
		ans.insert(ans.indexOf(DENTIST_ENDNODE), "</patientlist>");

		return ans.toString();
	}
}























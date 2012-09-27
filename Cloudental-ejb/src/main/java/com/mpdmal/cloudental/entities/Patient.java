package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.mpdmal.cloudental.util.CloudentUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Patient extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String comments;
	
	@NotNull
	@Temporal( TemporalType.TIMESTAMP)
	private Date created;
	
	@NotNull
	@NotEmpty
	private String name;
	
	@NotNull
	@NotEmpty
	private String surname;
	
	@NotNull
	@OneToOne(cascade=CascadeType.ALL, mappedBy="patient", fetch=FetchType.LAZY)
	private Medicalhistory medicalhistory;
	
	@NotNull
	@OneToOne(cascade=CascadeType.ALL, mappedBy="patient")
	private Patienthistory dentalhistory;
	
	@NotNull
    @ManyToOne
	@JoinColumn(name="dentistid")
	private Dentist dentist;
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="patient", fetch=FetchType.LAZY)
	private Set<Address> addresses;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="patient", fetch=FetchType.LAZY)
	private Set<Contactinfo> contactinfo;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="patient", fetch=FetchType.EAGER)
	private Set<Patienttooth> teeth;

    public Patient() {}

	public Date getCreated() 	{	return this.created;	}	
	public Integer getId() 			{	return this.id;	}
	public String getComments() 	{	return this.comments;	}
	public String getName() 	{	return this.name;	}
	public String getSurname() 	{	return this.surname;	}
	public Dentist getDentist() {	return this.dentist;	}
	public Medicalhistory getMedicalhistory() {		return this.medicalhistory;	}
	public Patienthistory getDentalHistory() {		return this.dentalhistory;	}
	public Set<Address> getAddresses() {	return this.addresses;	}
	public Set<Contactinfo> getContactInfo() {	return this.contactinfo;	}
	public Set<Patienttooth> getTeeth() {	return this.teeth;	}
	
	public void setId(Integer id) 				{	this.id = id;	}
	public void setComments(String comments) 	{	this.comments = comments;	}
	public void setCreated(Date created) 	{	this.created = created;	}
	public void setName(String name) 		{	this.name = name;	}
	public void setSurname(String surname) 	{	this.surname = surname;	}
	public void setDentist(Dentist dentist) {	this.dentist = dentist;	}
	public void setMedicalhistory(Medicalhistory medicalhistory){		this.medicalhistory = medicalhistory;	}
	public void setDentalhistory(Patienthistory dentalhistory) 	{
		dentalhistory.setPatient(this);
		this.dentalhistory= dentalhistory;	
	}
	public void setAddresses(Set<Address> adrs) {	
		if (addresses == null)
			addresses = new HashSet<Address>();
		else
			addresses.clear();
		
		for (Address address : adrs) {
			addAddress(address);
		}
	}
	public void addAddress(Address adrs) {	
		if (addresses == null)
			addresses = new HashSet<Address>();
		
		for (Address address : addresses) {
			if (address.getCity().equals(adrs.getCity())
					&& address.getCountry().equals(adrs.getCountry())
					&& address.getNumber().equals(adrs.getNumber())
					&& address.getPostalcode().equals(adrs.getPostalcode())
					&& address.getStreet().equals(adrs.getStreet())) {
				CloudentUtils.logWarning("Address already exists, wont add:"+adrs.getStreet());
				return;
			}
		}
		addresses.add(adrs);	
	}

	public void setContactInfo(Set<Contactinfo> cnt) {	
		if (contactinfo == null)
			contactinfo = new HashSet<Contactinfo>();
		else
			contactinfo.clear();
		for (Contactinfo contactinfo : cnt) {
			addContactInfo(contactinfo);
		}
	}
	public void addContactInfo(Contactinfo cnt) {	
		if (contactinfo == null)
			contactinfo = new HashSet<Contactinfo>();
		
		contactinfo.add(cnt);
	}
	
	public void updateContactInfo(Contactinfo cnt) {
		for (Contactinfo info : contactinfo) {
			if (info.getId().getInfotype().equals(cnt.getId().getInfotype())) { //replace
				info.setId(cnt.getId());
				info.setInfo(cnt.getInfo());
				return;
			}
		}
	}
	public void setTeeth(Set<Patienttooth> teeth) {
		if (this.teeth != null)
			this.teeth.clear();
		
		for (Patienttooth patienttooth : teeth) {
			addTooth(patienttooth);
		}
	}
	
	public void addTooth(Patienttooth tooth) {
		if (teeth == null)
			teeth = new HashSet<Patienttooth>();
		
		tooth.setPatient(this);
		teeth.add(tooth);
	}
	
	public String getUIfriendlyForm () {
		return id+" - "+name+" "+surname;
	}
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<patient></patient>");
		ans.insert(ans.indexOf("</patient"), "<id>"+id+"</id>");
		ans.insert(ans.indexOf("</patient"), "<name>"+name+"</name>");
		ans.insert(ans.indexOf("</patient"), "<surname>"+surname+"</surname>");
		ans.insert(ans.indexOf("</patient"), "<created>"+created+"</created>");
		ans.insert(ans.indexOf("</patient"), "<comments>"+comments+"</comments>");
		
		ans.insert(ans.indexOf("</patient"), "<contactinfo>");
		for (Contactinfo info: contactinfo) {
			ans.insert(ans.indexOf("</patient"), info.getXML());
		}
		for (Address address : addresses) {
			ans.insert(ans.indexOf("</patient"), address.getXML());
		}
		ans.insert(ans.indexOf("</patient"), "</contactinfo>");
		
		ans.insert(ans.indexOf("</patient"), medicalhistory.getXML());
		ans.insert(ans.indexOf("</patient"), "<mouth>");
		for (Patienttooth tooth : teeth) {
			ans.insert(ans.indexOf("</patient"), tooth.getXML());
		}
		ans.insert(ans.indexOf("</patient"), "</mouth>");
		ans.insert(ans.indexOf("</patient"), dentalhistory.getXML());
		return ans.toString();
	}
}
package com.mpdmal.cloudental.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.mpdmal.cloudental.util.CloudentUtils;

@Entity
public class Address extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AddressPK id;
	private String city;
	private String country;
	private Integer number;
	private String postalcode;
	private String street;

    @ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="id", insertable=false, updatable=false)
	private Patient patient;

    public Address() {}

	public AddressPK getId() 	{	return this.id;	}
	public String getCity() 	{	return this.city;	}
	public String getCountry() 	{	return this.country;	}
	public Integer getNumber() 	{	return this.number;	}
	public String getStreet() 	{	return this.street;	}
	public Patient getPatient() {	return this.patient;	}
	public String getPostalcode() {	return this.postalcode;	}
	
	public void setPostalcode(String postalcode) {		this.postalcode = postalcode;	}
	public void setPatient(Patient patient) {	this.patient = patient;	}
	public void setStreet(String street) 	{	this.street = street;	}
	public void setNumber(Integer number) 	{	this.number = number;	}
	public void setCountry(String country) 	{	this.country = country;	}
	public void setCity(String city) 		{	this.city = city;	}
	public void setId(AddressPK id) 		{	this.id = id;	}	
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<address></address>");
		ans.insert(ans.indexOf("</address"), "<type>"+CloudentUtils.findAddressTypeDescr(getId().getAdrstype())+"</type>");
		ans.insert(ans.indexOf("</address"), "<street>"+street+"</street>");
		ans.insert(ans.indexOf("</address"), "<number>"+number+"</number>");
		ans.insert(ans.indexOf("</address"), "<country>"+country+"</country>");
		ans.insert(ans.indexOf("</address"), "<city>"+city+"</city>");
		ans.insert(ans.indexOf("</address"), "<pcode>"+postalcode+"</pcode>");
		return ans.toString();
	}
	
	public String getUIFriendlyString() {
		return number+","+street+" "+postalcode+"    - "+country+", "+city;
	}
}
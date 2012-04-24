package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Discount extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String description;
	@NotNull
	private String title;
	@NotNull
	private BigDecimal discount;
	@NotNull
    @ManyToOne
	@JoinColumn(name="dentistid")
	private Dentist dentist;

    public Discount() {}

	public Integer getId() 	{	return this.id;	}
	public String getTitle() {	return this.title;	}
	public BigDecimal getDiscount() 	{	return this.discount;	}
	public String getDescription() 	{	return this.description;	}
	public Dentist getDentist() {		return this.dentist;	}

	public void setId(Integer id) 	{	this.id = id;	}
	public void setTitle(String title){	this.title = title;	}
	public void setDiscount(BigDecimal discount) 			{	this.discount = discount;	}
	public void setDescription(String description)	{	this.description = description;	}
	public void setDentist(Dentist dentist) {		this.dentist = dentist;	}
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<discount></discount>");
		ans.insert(ans.indexOf("</discount"), "<title>"+title+"</title>");
		ans.insert(ans.indexOf("</discount"), "<description>"+description+"</description>");
		ans.insert(ans.indexOf("</discount"), "<value>"+discount+"</value>");
		return ans.toString();
	}
}
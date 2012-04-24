package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Pricelist extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String description;
	private String title;
	private BigDecimal price;
    @ManyToOne
	@JoinColumn(name="dentistid", insertable=false, updatable=false)
	private Dentist dentist;

    public Pricelist() {}

	public Integer getId() 	{	return this.id;	}
	public String getTitle() {	return this.title;	}
	public BigDecimal getPrice() 	{	return this.price;	}
	public String getDescription() 	{	return this.description;	}
	public Dentist getDentist() {		return this.dentist;	}

	public void setId(Integer id) 	{	this.id = id;	}
	public void setTitle(String title){	this.title = title;	}
	public void setPrice(BigDecimal price) 			{	this.price = price;	}
	public void setDescription(String description)	{	this.description = description;	}
	public void setDentist(Dentist dentist) {		this.dentist = dentist;	}
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<pricable></pricable>");
		ans.insert(ans.indexOf("</pricable"), "<title>"+title+"</title>");
		ans.insert(ans.indexOf("</pricable"), "<description>"+description+"</description>");
		ans.insert(ans.indexOf("</pricable"), "<price>"+price+"</price>");
		return ans.toString();
	}
}
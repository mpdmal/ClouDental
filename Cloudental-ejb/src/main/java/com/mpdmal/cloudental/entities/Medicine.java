package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="medicine")
public class Medicine extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String activeingredient;
	private String name;

	//GETTERS/SETTERS
	public void setId(Integer id) {	this.id = id;	}
	public void setActiveingredient(String activeingredient) {	this.activeingredient = activeingredient;	}
	public void setName(String name) {	this.name = name;	}

	public Integer getId() {	return this.id;	}
	public String getActiveingredient() {	return this.activeingredient;	}
	public String getName() {	return this.name;	}

	//INTERFACE
	@Override
	public String getXML() {
		return null;
	}
}
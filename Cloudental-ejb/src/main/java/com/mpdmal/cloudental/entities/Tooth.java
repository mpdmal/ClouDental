package com.mpdmal.cloudental.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Tooth extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer position;
	private String name;

    public Tooth() {}

	public Integer getPosition() {	return this.position;	}
	public String getName() 	 {	return this.name;	}
	public void setPosition(int position) {	this.position = position;	}
	public void  setName(String name) 	 { this.name = name; }	
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<tooth></tooth>");
		ans.insert(ans.indexOf("</tooth"), "<positioh>"+position+"</position>");
		ans.insert(ans.indexOf("</tooth"), "<name>"+name+"</name>");
		return ans.toString();		
	}

}
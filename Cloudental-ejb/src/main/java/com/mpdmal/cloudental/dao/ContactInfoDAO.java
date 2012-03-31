package com.mpdmal.cloudental.dao;

import java.util.Vector;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Contactinfo;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
@LocalBean
public class ContactInfoDAO extends CDentAbstractDao {
	static final String WHERE = " where ci.patient.id = :id";
	static final String COUNT_CINFO= "select count(ci) from Contactinfo ci";
	static final String GET_CINFO = "select ci from Contactinfo ci";
	
	public ContactInfoDAO() {	super();	}
    public ContactInfoDAO(EntityManager em) {	super(em);	}

    @SuppressWarnings("unchecked")
    public Vector<Contactinfo> getContactinfo(int patientid) {
    	return (Vector<Contactinfo>)_em.createQuery(GET_CINFO+WHERE)
    			.setParameter("id", patientid).getResultList();    	
    }
}

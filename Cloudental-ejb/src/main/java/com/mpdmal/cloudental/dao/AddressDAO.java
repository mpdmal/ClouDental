package com.mpdmal.cloudental.dao;

import java.util.Vector;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Address;
import com.mpdmal.cloudental.util.CloudentUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
@LocalBean
public class AddressDAO extends CDentAbstractDao {
	static final String WHERE_HOME = "and adr.adrstype = "+CloudentUtils.AddressType.HOME.getValue();
	static final String WHERE_OFFICE = "and adr.adrstype = "+CloudentUtils.AddressType.OFFICE.getValue();
	static final String WHERE_BILLING = "and adr.adrstype = "+CloudentUtils.AddressType.BILLING.getValue();
	static final String WHERE = " where adr.patient.id = :id";
	static final String COUNT_CINFO= "select count(adr) from Address adr";
	static final String GET_CINFO = "select adr from Address adr";

	public AddressDAO() {	super();    }       
    public AddressDAO(EntityManager em) {	super(em);    }
	
    public Vector<Address> getEveryAddress(int patientid) {
    	return doLocalGet(GET_CINFO+WHERE, patientid);    
    }
    
    public Vector<Address> getHomeAddress(int patientid) {
    	return doLocalGet(GET_CINFO+WHERE_HOME, patientid);    
    }

    public Vector<Address> getOfficeAddress(int patientid) {
    	return doLocalGet(GET_CINFO+WHERE_OFFICE, patientid);    
    }

    public Vector<Address> getHomeBilling(int patientid) {
    	return doLocalGet(GET_CINFO+WHERE_BILLING, patientid);    	
    }

    @SuppressWarnings("unchecked")
    private Vector<Address> doLocalGet(String query, int id) {
    	return (Vector<Address>)_em.createQuery(query).setParameter("id", id).getResultList();
    }
}

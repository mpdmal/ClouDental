package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Medicalhistoryentry;

@Stateless
@LocalBean
public class MedicalhistoryentryDAO  extends CDentAbstractDao {
	static final String WHERE = " where co.id = :medhistoryid";
	static final String COUNT_ENTRIES= "select count(co) from Medicalhistoryentry co";
	static final String GET_ENTRIES = "select co from Medicalhistoryentry co";

	static final String COUNT_ENTRIES_INHIST= COUNT_ENTRIES + WHERE;
	static final String GET_ENTRIES_INHIST = GET_ENTRIES + WHERE;

    public MedicalhistoryentryDAO() { super(); }
    public MedicalhistoryentryDAO(EntityManager em) { super(em); }
 
    public long countMedicalhistoryentrys() {
        return executeSingleLongQuery(_em.createQuery(COUNT_ENTRIES));
    }
    public long countMedicalhistoryentrys(int id) {
        return executeSingleLongQuery(_em.createQuery(COUNT_ENTRIES_INHIST)
        		.setParameter("medhistoryid", id));
    }
    @SuppressWarnings("unchecked")
	public Vector<Medicalhistoryentry> getMedicalhistoryentrys(int id) {
    	return (Vector<Medicalhistoryentry>)_em.createQuery(GET_ENTRIES_INHIST)
    			.setParameter("medhistoryid", id).getResultList();
    }

    @SuppressWarnings("unchecked")
	public Vector<Medicalhistoryentry> getMedicalhistoryentrys() {
    	return (Vector<Medicalhistoryentry>)_em.createQuery(GET_ENTRIES).getResultList();
    }
}

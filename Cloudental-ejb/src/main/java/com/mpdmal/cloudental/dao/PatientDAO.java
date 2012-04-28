package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Patient;

@Stateless
@LocalBean
public class PatientDAO extends CDentAbstractDao {
	static final String WHERE_SHORT = "where p.dentist.username = :dentistid";
	static final String WHERE_LONG = "where p.dentist.username = :dentistid and p.surname = :surname and p.name = :name";
	static final String COUNT_PATIENTS = "select count(p) from Patient p ";
	static final String GET_PATIENTS = "select p from Patient p ";
	
	static final String GET_DENTISTPATIENTS = GET_PATIENTS+WHERE_SHORT;
	static final String GET_DENTISTPATIENT = GET_PATIENTS+WHERE_LONG;
	static final String COUNT_DENTISTPATIENTS = COUNT_PATIENTS + WHERE_SHORT;
	static final String EXISTS_DENTISTPATIENT = COUNT_PATIENTS+WHERE_LONG;
	
	static final String GETPATIENT_BYID = GET_PATIENTS+" where p.id = :id";
	
    public PatientDAO() { super(); }
    public PatientDAO(EntityManager em) { super(em); }
 
    public long countPatients() {
        return executeSingleLongQuery(_em.createQuery(COUNT_PATIENTS));
    }
    public long countPatients(String dentistid) {
        return executeSingleLongQuery(_em.createQuery(COUNT_DENTISTPATIENTS)
        		.setParameter("dentistid", dentistid));
    }

    @SuppressWarnings("unchecked")
	public Vector<Patient> getPatients() {
    	return (Vector<Patient>)_em.createQuery(GET_PATIENTS).getResultList();
    }

    @SuppressWarnings("unchecked")
	public Vector<Patient> getPatients(String dentistid) {
    	return (Vector<Patient>)_em.createQuery(GET_DENTISTPATIENTS)
    			 .setParameter("dentistid", dentistid).getResultList();
    }
    
    public boolean existsPatient(String dentistid, String name, String surname) {
    	 Query q = _em.createQuery(COUNT_DENTISTPATIENTS)
    			 .setParameter("dentistid", dentistid)
    			 .setParameter("surname", surname)
    			 .setParameter("name", name);
         return  executeSingleLongQuery(q) > 0 ? true : false;
    }
    public Patient getPatient(String dentistid, String name, String surname) {
   	 Query q = _em.createQuery(GET_DENTISTPATIENT)
   			.setParameter("dentistid", dentistid)
   			 .setParameter("surname", surname)
   			 .setParameter("name", name);
        return  (Patient)executeSingleObjectQuery(q);
   }
    
    public Patient getPatient(int id) {
    	Query q = _em.createQuery(GETPATIENT_BYID)
    			.setParameter("id", id);
         return  (Patient)executeSingleObjectQuery(q);

    }
}

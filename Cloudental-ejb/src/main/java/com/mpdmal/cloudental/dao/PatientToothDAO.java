package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Patienttooth;

@Stateless
@LocalBean
public class PatientToothDAO extends CDentAbstractDao {
	static final String GET_PATIENTTEETH = "select pt from patienttooth pt where pt.id.patientid= :patientid";

	public PatientToothDAO(EntityManager em) { super(em); }

   @SuppressWarnings("unchecked")
	public Vector<Patienttooth> getPatientTeeth(int patientid) {
    	return (Vector<Patienttooth>)_em.createQuery(GET_PATIENTTEETH)
    			.setParameter("dentistid", patientid).getResultList();
    }

}

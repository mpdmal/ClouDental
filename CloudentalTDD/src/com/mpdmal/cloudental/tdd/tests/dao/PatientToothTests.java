package com.mpdmal.cloudental.tdd.tests.dao;

import java.sql.Timestamp;
import java.util.Vector;

import org.junit.Test;

import com.mpdmal.cloudental.dao.TeethDAO;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienttooth;
import com.mpdmal.cloudental.entities.PatienttoothPK;
import com.mpdmal.cloudental.entities.Tooth;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.exception.PatientExistsException;

public class PatientToothTests extends CDentAbstractDaoTest {
	Dentist d = new Dentist();
	Patient p = new Patient();
	@Override
	public void initTestEnv() {
		Timestamp tstamp = new Timestamp(System.currentTimeMillis());

		d.setName("test");
		d.setSurname("testD");
		d.setUsername("testDentist");
		d.setPassword("zxc");
		
		p.setCreated(tstamp);
		p.setName("pt");
		p.setSurname("pp");
				
		p.setDentist(d);
		try {
			d.addPatient(p);
		} catch (PatientExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_dentistdao.updateCreate(d, false);
	}
	@Test
	public void testPatientTeeth() {
		Vector<Tooth> teethset = _tdao.getDefaultTeethSet();
		Tooth tooth = teethset.elementAt(0);
		
		Patienttooth ptooth = new Patienttooth();
		ptooth.setComments("");
		ptooth.setImage(null);
		ptooth.setPatient(p);

		PatienttoothPK id = new PatienttoothPK();
		id.setPatientid(p.getId());
		id.setToothid(tooth.getPosition());
		
		ptooth.setId(id);
		ptooth.setPatient(p);
		p.addTooth(ptooth);
		_ptdao.updateCreate(ptooth, false);

	}
	
	@Override
	public void closeTestEnv() {
		Patient pp = _patientdao.getPatient(p.getId());
		_patientdao.delete(pp);
		Dentist dd = _dentistdao.getDentist("testDentist");
		_dentistdao.delete(dd);
	}
}
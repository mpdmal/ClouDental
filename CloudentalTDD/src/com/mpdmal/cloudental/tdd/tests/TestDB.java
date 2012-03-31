package com.mpdmal.cloudental.tdd.tests;

import org.junit.Test;

import com.mpdmal.cloudental.tdd.base.CDentAbstractBeanTest;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

public class TestDB extends CDentAbstractBeanTest {
	@Override
	public void initTestEnv() {
		//empty db
		_dbean.deleteDentists();
	}
	@Test
	public void testDB() throws InvalidPostitAlertException {
		_dbean.createDentist("Demo Dentist", "demo", "Demopoulos", "Demis");
		_dbean.createDentist("Demo Dentist2", "demo2", "Demidis", "Demos");
		
		_dsvcbean.createPatient("Demo Dentist", "Ko", "Pat");
		_dsvcbean.createPatient("Demo Dentist2", "Dim", "Aza");
		_dsvcbean.createPatient("Demo Dentist2", "Ge", "Mit" );
		
		_dsvcbean.createNote("Demo Dentist", "note 1.1", 1);
		_dsvcbean.createNote("Demo Dentist", "note 1.2", 2);
		_dsvcbean.createNote("Demo Dentist", "note 1.3", 3);
		_dsvcbean.createNote("Demo Dentist2", "note 2.1", 1);

	}
	
	@Override
	public void closeTestEnv() {
		//cascade delete
//		_dbean.deleteDentist(_dbean.getDentist("Demo Dentist"));
	}
}

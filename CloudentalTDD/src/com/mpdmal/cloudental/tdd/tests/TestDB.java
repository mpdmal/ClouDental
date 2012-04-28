package com.mpdmal.cloudental.tdd.tests;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.tdd.base.CDentAbstractBeanTest;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.PatientAlreadyExistsException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;

public class TestDB extends CDentAbstractBeanTest {
	@Override
	public void initTestEnv() {
		//empty db
		_dbean.deleteDentists();
	}
	@Test
	public void testDB() throws InvalidPostitAlertException {
		Dentist d1 = _dbean.createDentist("Demo Dentist", "demo", "Demopoulos", "Demis");
		Dentist d2 = _dbean.createDentist("Demo Dentist2", "demo2", "Demidis", "Demos");

		PricelistItem d1p1 = _dsvcbean.createPricelistItem("Demo Dentist", "pl item 1", "", 10.0);
		PricelistItem d1p2 =_dsvcbean.createPricelistItem("Demo Dentist", "pl item 2", "", 20.0);
		PricelistItem d2p1 =_dsvcbean.createPricelistItem("Demo Dentist2", "pl item 1", "", 30.0);
		PricelistItem d2p2 =_dsvcbean.createPricelistItem("Demo Dentist2", "pl item 2", "", 40.0);

		Discount d1d1 = _dsvcbean.createDiscount("Demo Dentist", "dc item 1", "", 0.10);
		Discount d1d2 = _dsvcbean.createDiscount("Demo Dentist", "dc item 2", "", 0.20);
		Discount d2d1 = _dsvcbean.createDiscount("Demo Dentist2", "dc item 1", "", 0.30);
		Discount d2d2 = _dsvcbean.createDiscount("Demo Dentist2", "dc item 2", "", 0.40);

		Patient p1;
		Patient p2;
		Patient p3;
		try {
			p1 = _dsvcbean.createPatient("Demo Dentist", "Ko", "Pat");
			p2 = _dsvcbean.createPatient("Demo Dentist2", "Dim", "Aza");
			p3 = _dsvcbean.createPatient("Demo Dentist2", "Ge", "Mit" );
			
			Thread.sleep(50);
			_dsvcbean.createNote("Demo Dentist", "note 1.1", CloudentUtils.PostitAlertType.ALARM.getValue());
			Thread.sleep(50);
			_dsvcbean.createNote("Demo Dentist", "note 1.2", CloudentUtils.PostitAlertType.TODO.getValue());
			Thread.sleep(50);
			_dsvcbean.createNote("Demo Dentist2", "note 1.3", CloudentUtils.PostitAlertType.NOTE.getValue());

			Activity act1 = _psvcbean.createActivity(p1.getId(), "activity 1", new Date(), null, d1p1, d1d1);
			Activity act2 = _psvcbean.createActivity(p3.getId(), "activity 2", new Date(), null, d2p1, d2d1);
			Activity act3 = _psvcbean.createActivity(p3.getId(), "activity 3", new Date(), null, d2p1, d2d1);
			Activity act4 = _psvcbean.createActivity(p3.getId(), "activity 4", new Date(), null, d2p1, d2d2);
			Activity act5 = _psvcbean.createActivity(p2.getId(), "activity 5", new Date(), null, d2p2, d2d1);
			Activity act6 = _psvcbean.createActivity(p2.getId(), "activity 6", new Date(), null, d2p1, d2d2);
//
			Visit v1 = _psvcbean.createVisit(act3.getId(), "", "a visit1", new Date(), null, 121.21, 2);
			Visit v2 = _psvcbean.createVisit(act1.getId(), "", "a visit2", new Date(), null, 221.21, 2);
			Visit v3 = _psvcbean.createVisit(act5.getId(), "", "a visit3.1", new Date(), null, 1.21, 2);
			Visit v4 = _psvcbean.createVisit(act6.getId(), "", "a visit4", new Date(), null, 321.21, 2);
			Visit v5 = _psvcbean.createVisit(act5.getId(), "", "a visit3.2", new Date(System.currentTimeMillis()+10000), null, 21.21, 2);
			Visit v6 = _psvcbean.createVisit(act5.getId(), "", "a visit3.3", new Date(System.currentTimeMillis()+50000), null, 321.21, 2);
			_psvcbean.deleteVisits(act5.getId());

		} catch (PatientAlreadyExistsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActivityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PatientNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void closeTestEnv() {
		//cascade delete
		_dbean.deleteDentist(_dbean.getDentist("Demo Dentist").getUsername());
		_dbean.deleteDentist("Demo Dentist2");
		assertEquals(0, _dbean.countDentists());
		assertEquals(0, _dsvcbean.countDiscounts());
		assertEquals(0, _dsvcbean.countNotes());
		assertEquals(0, _dsvcbean.countPricelistItems());
		//TODO  count activities and count patients
	}
}

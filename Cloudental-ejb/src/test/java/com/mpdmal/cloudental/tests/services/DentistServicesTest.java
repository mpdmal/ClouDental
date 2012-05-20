package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Vector;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class DentistServicesTest extends ArquillianCloudentTest{

	@Test
	public void createPricelistItem() throws CloudentException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		assertEquals(0, dsvcbean.countPricelistItems());
		Dentist d = dbean.createDentist("qwe", "asd", "zxc", "vsd");
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPricelistItem(d.getId(), "pc"+i, "wef"+i, i*10.1);			
		}
		
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());
	}

	@Test
	public void updatePricelistItems() throws CloudentException {

		Dentist d = dbean.getDentist("zxc");
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());

		PricelistItem item = pricelist.iterator().next();
		double price = item.getPrice().doubleValue();
		dsvcbean.updatePricelistItem(item.getId(), "altered!", "rty");
		pricelist = dsvcbean.getPricelist(d.getId());
		for (PricelistItem pricelistItem : pricelist) {
			if (pricelistItem.getPrice().doubleValue() == price)
				assertEquals(pricelistItem.getDescription(), "altered!");
		}
	}

	@Test
	public void deletePricelistItems() throws CloudentException {

		assertEquals(1, dbean.countDentists());
		Dentist d = dbean.getDentist("zxc");
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());

		dsvcbean.deletePricelistItem(pricelist.iterator().next().getId());
		pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(9, pricelist.size());
		assertEquals(1, dbean.countDentists());

		for (PricelistItem pricelistItem : pricelist) {
			if (pricelistItem.getPrice().doubleValue() < 40)
				dsvcbean.deletePricelistItem(pricelistItem.getId());
		}
		assertEquals(6, dsvcbean.countPricelistItems());
		dbean.deleteDentists();
		assertEquals(0, dsvcbean.countPricelistItems());
	}

	@Test
	public void createPatient() throws CloudentException {
		Dentist d = dbean.createDentist("qwe", "asd", "zxc", "vsd");
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPatient(d.getId(), "PAtient", "PAtient surname");	
		}
	}

	@Test 
	public void deletePatient() throws CloudentException {

		Dentist d = dbean.getDentist("zxc");
		Vector<Patient> patients = (Vector<Patient>) d.getPatientList();
		assertEquals(10, patients.size());
		dsvcbean.deletePatient(patients.elementAt(0).getId());
		dsvcbean.deletePatient(patients.elementAt(2).getId());
		dsvcbean.deletePatient(patients.elementAt(3).getId());
		dsvcbean.deletePatient(patients.elementAt(5).getId());

		patients = (Vector<Patient>) dbean.getDentist("zxc").getPatientList();
		assertEquals(6, patients.size());

		dsvcbean.deletePatientList(d.getId());
		patients = (Vector<Patient>) dbean.getDentist("zxc").getPatientList();
		assertEquals(0, patients.size());
		dbean.deleteDentists();
	}

}

package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;

import com.mpdmal.cloudental.tests.base.ArquillianCloudentTestBase;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class ActivityTestSet extends ArquillianCloudentTestBase{
	
	@Test
	@InSequence (1)
	public void create() throws CloudentException {
		Dentist d = dbean.createDentist("dim", "az", "arilou", "coersum");
		Patient p = dsvcbean.createPatient(d.getId(), "ko", "pat");
		Discount dc = dsvcbean.createDiscount(d.getId(), "discount 1", "sddf", .30);
		PricelistItem item = dsvcbean.createPricelistItem(d.getId(), "item 1", "asdasd", 23.12);
		
		for (int i = 0; i < 10; i++) {
			psvcbean.createActivity(p.getId(), "activity "+i, new Date(), 
							new Date(System.currentTimeMillis()+1000000), item.getId(), dc.getId(), null);	
		}
		
		//find by patient
		Vector<Activity> acts = (Vector<Activity>) dsvcbean.findPatient(p.getId()).getDentalHistory().getActivities();
		assertEquals(10, acts.size());
		for (int i = 0; i < acts.size(); i++) {
			Activity act = acts.elementAt(i);
			assertEquals("activity "+i, act.getDescription());
			assertEquals(false, act.isOpen());
			assertEquals(null, act.getPrice());
		}
		
		//create identical 
		Date start = new Date(), end = new Date(System.currentTimeMillis()+10000);
		psvcbean.createActivity(p.getId(), "activity 10", start,end, item.getId(), dc.getId(), null);	
		psvcbean.createActivity(p.getId(), "activity 10", start,end, item.getId(), dc.getId(), null);
		
		acts = (Vector<Activity>) dsvcbean.findPatient(p.getId()).getDentalHistory().getActivities();
		assertEquals(12, acts.size());
		
		//failed create ..constraints violated
		try {
			psvcbean.createActivity(p.getId(), null, null, null, item.getId(), dc.getId(), null);
		} catch (ValidationException e) {
			String msg = e.getMessage();
			assertEquals(true, msg.contains("Property->DESCRIPTION on Entity->ACTIVITY may not be null"));
			assertEquals(true, msg.contains("Property->STARTDATE on Entity->ACTIVITY may not be null"));
			assertEquals(true, msg.contains("Property->ENDDATE on Entity->ACTIVITY may not be null"));
		}

		acts = (Vector<Activity>) dsvcbean.findPatient(p.getId()).getDentalHistory().getActivities();
		assertEquals(12, acts.size());

		//failed create ..patient, pricable, discount does not exist
		start = new Date();
		end = new Date(System.currentTimeMillis()+10000);
		try {
			psvcbean.createActivity(-1, "activity 11", start,end, item.getId(), dc.getId(), null);
		} catch (PatientNotFoundException e) {
			assertEquals("Patient not found:-1\n", e.getMessage());
		}
		try {
			psvcbean.createActivity(p.getId(), "activity 11", start,end, -1, dc.getId(), null);
		} catch (PricelistItemNotFoundException e) {
			assertEquals("Pricelist Item id not found:-1\n", e.getMessage());
		}
		try {
			psvcbean.createActivity(p.getId(), "activity 11", start,end, item.getId(), -1, null);
		} catch (DiscountNotFoundException e) {
			assertEquals("Discount not found:-1\n", e.getMessage());
		}
		
		acts = (Vector<Activity>) dsvcbean.findPatient(p.getId()).getDentalHistory().getActivities();
		assertEquals(12, acts.size());
	}
	
	@Test
	@InSequence (2)
	public void getAndCount () throws DentistNotFoundException, PatientNotFoundException {
		Dentist d = dbean.findDentistByUsername("arilou");
		//get by patient id
		Patient p = ((Vector<Patient>)dsvcbean.getPatientlist(d.getId())).elementAt(0);
		Vector<Activity> acts = (Vector<Activity>)psvcbean.getPatientActivities(p.getId());
		assertEquals(12, acts.size());
		
		//get from dentist object
		acts = (Vector<Activity>)((Vector<Patient>)d.getPatientList()).elementAt(0).getDentalHistory().getActivities();
		assertEquals(12, acts.size());
		
		for (int i = 0; i < acts.size()-1; i++) {
			Activity act = acts.elementAt(i);
			assertEquals("activity "+i, act.getDescription());
			assertEquals(false, act.isOpen());
			assertEquals(null, act.getPrice());
		}
		
		assertEquals(12, psvcbean.countPatientActivities(p.getId()));
		//count patients
		assertEquals(12, psvcbean.countActivities());
	}
	
	@Test 
	@InSequence (3)
	public void update() throws CloudentException {
		Dentist d = dbean.findDentistByUsername("arilou");
		Patient p = d.getPatientList().iterator().next();
		Discount dc = dsvcbean.createDiscount(d.getId(), "discount 2", "asd", 23.30);
		PricelistItem item = dsvcbean.createPricelistItem(d.getId(), "item 2", "asd", 582.);

		//update patients
		for (Activity act : p.getDentalHistory().getActivities()) {
			act.setDiscount(dc);
			act.setPriceable(item);
			act.setDescription("altered");
			act.setPrice(BigDecimal.valueOf(20.20));
			psvcbean.updateActivity(act);
		}

		//through service
		Collection<Activity> acts = psvcbean.getPatientActivities(p.getId());
		for (Activity act : acts) {
			assertEquals("altered", act.getDescription());
			assertEquals(dc.getId(), act.getDiscount().getId());
			assertEquals(item.getId(), act.getPriceable().getId());
			assertEquals(20.20, act.getPrice().doubleValue(), .0001);
		}
		
		//through patient
		p = psvcbean.findPatient(p.getId());
		acts = p.getDentalHistory().getActivities(); 
		for (Activity act : acts) {
			assertEquals("altered", act.getDescription());
			assertEquals(dc.getId(), act.getDiscount().getId());
			assertEquals(item.getId(), act.getPriceable().getId());
			assertEquals(20.20, act.getPrice().doubleValue(), .0001);
		}
		
		assertEquals(12, psvcbean.countPatientActivities(p.getId()));
		assertEquals(12, psvcbean.countActivities());
	}
	
	@Test
	@InSequence (4)
	public void delete() throws CloudentException {
		Dentist d = dbean.findDentistByUsername("arilou");
		Patient p = d.getPatientList().iterator().next();
		Vector<Activity> acts = psvcbean.getPatientActivities(p.getId());
		
		int anItemId = acts.elementAt(0).getPriceable().getId(),
			aDiscountId = acts.elementAt(0).getDiscount().getId();
		
		//delete by id
		for (int i = 0; i < 5; i++) {
			psvcbean.deleteActivity(acts.elementAt(i).getId());
		}
		
		assertEquals(7, psvcbean.countActivities());
		
		//failed delete ...invalid id
		try {
			psvcbean.deleteActivity(-1);
		} catch (ActivityNotFoundException e) {
			assertEquals("Activity not found:-1\n", e.getMessage());
		}
		
		assertEquals(7, psvcbean.countActivities());
		try {
			psvcbean.deletePatientActivities(p.getId());
		} catch (PatientNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check else everything unaffected
		assertEquals(0, psvcbean.countActivities());
		assertEquals(0, psvcbean.countPatientActivities(p.getId()));
		assertEquals(1, dsvcbean.countPatients());
		assertEquals(2, dsvcbean.countDiscounts());
		assertEquals(2, dsvcbean.countPricelistItems());
		assertEquals(1, dbean.countDentists());
		
		//cascade delete by patient
		for (int i = 0; i < 10; i++) 
			psvcbean.createActivity(p.getId(), "activity "+i, new Date(), 
					new Date(System.currentTimeMillis()+1000000), anItemId, aDiscountId, null);

		assertEquals(10, psvcbean.countActivities());
		dbean.deleteDentists();
		
	}
}

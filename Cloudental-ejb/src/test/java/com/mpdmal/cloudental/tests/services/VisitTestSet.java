package com.mpdmal.cloudental.tests.services;

import java.util.Collection;
import java.util.Date;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTestBase;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import static org.junit.Assert.assertEquals;

import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;

public class VisitTestSet extends ArquillianCloudentTestBase {
	@Test 
	@InSequence (1)
	public void create() throws CloudentException {
		dbean.deleteDentists();
		Dentist d = dbean.createDentist("dim", "az", "arilou", "coersum");
		Patient p = dsvcbean.createPatient(d.getId(), "ko", "pat");
		Discount dc = dsvcbean.createDiscount(d.getId(), "discount 1", "sddf", .30);
		PricelistItem item = dsvcbean.createPricelistItem(d.getId(), "item 1", "asdasd", 23.12);
		long start = System.currentTimeMillis();
		
		//valid visits for activity1
		Activity act1 = psvcbean.createActivity(p.getId(), "activity 1", new Date(start), new Date(start+110000), item.getId(), dc.getId(), null);
		psvcbean.createVisit(act1.getId(), "some descr1", "a visit1_1", new Date(start+50000), new Date(start+60000), 100.0, 2);
		psvcbean.createVisit(act1.getId(), "some descr2", "a visit1_2", new Date(start+80000), new Date(start+90000), 200.0, 2);
		assertEquals(2, psvcbean.countPatientVisits(p.getId()));
		
		//failed create , overlapping visits
		try {
			psvcbean.createVisit(act1.getId(), "some descr", "a visit1_fail1",new Date(start+80500),new Date(start+89000), 100.0, 2);
		} catch (ValidationException e1) {
			assertEquals("Visit dates cannot overlap one another", e1.getMessage());
		}
		assertEquals(2, psvcbean.countPatientVisits(p.getId()));
		try {
			psvcbean.createVisit(act1.getId(), "some descr", "a visit1_fail2",new Date(start+40000),new Date(start+70000), 100.0, 2);
		} catch (ValidationException e1) {
			assertEquals("Visit dates cannot overlap one another", e1.getMessage());
		}
		assertEquals(2, psvcbean.countPatientVisits(p.getId()));
		assertEquals(2, psvcbean.countActivityVisits(act1.getId()));
		
		//valid visits for activity2
		Activity act2 = psvcbean.createActivity(p.getId(), "activity 2", new Date(),new Date(start+220000000), item.getId(), dc.getId(), null);	
		psvcbean.createVisit(act2.getId(), "some descr3", "a visit2_1",new Date(start+180000000),new Date(start+190000000), 10.0, 1);
		psvcbean.createVisit(act2.getId(), "some descr4", "a visit2_2",new Date(start+80000000),new Date(start+90000000), 20.0, 1);
		assertEquals(2, psvcbean.countActivityVisits(act2.getId()));
		//failed create , overlapping visits of different activities
		try {
			psvcbean.createVisit(act2.getId(), "some descr2", "a visit2_failed", new Date(start+85000), new Date(start+95000), 200.0, 2);
		} catch (ValidationException e1) {
			assertEquals("Visit dates cannot overlap one another", e1.getMessage());
		}
		assertEquals(2, psvcbean.countActivityVisits(act2.getId()));
		//failed create ..outside activity dates
		try {
			psvcbean.createVisit(act2.getId(), "some descr", "a visit2_failed",new Date(start + 180000000), new Date(start + 340000000), 10.0, 1);
		} catch (ValidationException e) {
			assertEquals("Visit START and END date must be within the respective Activity dates", e.getMessage());
		}
		assertEquals(2, psvcbean.countActivityVisits(act2.getId()));
		
		try {
			psvcbean.createVisit(act2.getId(), "some descr", "a visit2_", new Date(start-10000), new Date(start+190000000), 10.0, 1);
		} catch (ValidationException e) {
			assertEquals("Visit START and END date must be within the respective Activity dates", e.getMessage());
		}
		assertEquals(4, psvcbean.countPatientVisits(p.getId()));
		assertEquals(2, psvcbean.countActivityVisits(act2.getId()));
		assertEquals(2, psvcbean.countActivityVisits(act1.getId()));
	}
	
	@Test
	@InSequence (2)
	public void getAndCount () throws ActivityNotFoundException, DentistNotFoundException {
		//get by dentist
		Dentist d = dbean.findDentistByUsername("arilou");
		Patient p = d.getPatientList().iterator().next();
		for (Activity act : p.getDentalHistory().getActivities()) {
			assertEquals(2, psvcbean.countActivityVisits(act.getId()));
			assertEquals(2, psvcbean.getActivityVisits(act.getId()).size());
		}
		
		//get by service
		Collection<Visit> visits = psvcbean.getPatientVisits(p.getId());
		for (Visit visit : visits) {
			assertEquals(p.getId(), visit.getActivity().getPatienthistory().getPatient().getId());
			assertEquals(true, visit.getTitle().contains("a visit"));
			assertEquals(true, visit.getComments().contains("some descr"));
		}
		assertEquals(4, visits.size());
		assertEquals(4, psvcbean.countVisits());
		assertEquals(4, psvcbean.countPatientVisits(p.getId()));
		
	}
	
	@Test 
	@InSequence (3)
	public void update() {}
	@Test
	@InSequence (4)
	public void delete() {}
}




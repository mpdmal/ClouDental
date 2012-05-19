package com.mpdmal.cloudental.tdd.tests.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

import static org.junit.Assert.assertEquals;

public class PatientServicesTests {
	EaoManager egr ;

	@Before
	public void pre() {
		egr = new EaoManager(Persistence.createEntityManagerFactory("CloudentalTDD").createEntityManager());
	}
	
	@After
	public void post() {
		egr.closeEM();
	}
	@Test
	public void crudActivity() throws CloudentException {
		DentistBean dbean = new DentistBean(egr);
		DentistServices dsvcbean = new DentistServices(egr);
		PatientServices psvcbean = new PatientServices(egr);
		
		assertEquals(0, dbean.countDentists());
		Dentist d = dbean.createDentist("dim", "az", "arilou", "coersum");
		Patient p = dsvcbean.createPatient(d.getId(), "ko", "pat");
		Discount dc = dsvcbean.createDiscount(d.getId(), "discount 1", "sddf", .30);
		PricelistItem item = dsvcbean.createPricelistItem(d.getId(), "item 1", "asdasd", 23.12);
		
		for (int i = 0; i < 10; i++) {
			psvcbean.createActivity(p.getId(), "activity "+i, new Date(), null, item.getId(), dc.getId());			
		}
		
		//get activities by dentist 
		d = dbean.getDentist("arilou");
		Vector<Patient> pts = (Vector<Patient>)d.getPatientList();
		p = pts.get(0); //only 1 patient
		assertEquals(10, p.getDentalHistory().getActivities().size());
		
		//get activities by patient id
		Vector<Activity> activities = (Vector<Activity>)psvcbean.getActivities(p.getId());
		Activity act = activities.iterator().next();
		assertEquals("activity 0", act.getDescription());
		
		//update activity
		act.setDescription("altered!");
		psvcbean.updateActivity(act);
		
		activities = psvcbean.getActivities(p.getId());
		act = activities.elementAt(9); //altered entry comes last 
		assertEquals("altered!", act.getDescription());
		
		//cascade delete activities by patient 
		dsvcbean.deletePatient(p.getId());
		
		dbean.deleteDentist(d);
	}
	
	@Test
	public void createMedicalHistoryEntry() throws CloudentException {
		DentistBean dbean = new DentistBean(egr);
		DentistServices dsvcbean = new DentistServices(egr);
		PatientServices psvcbean = new PatientServices(egr);
		
		assertEquals(0, dbean.countDentists());
		Dentist d = dbean.createDentist("dim", "az", "arilou", "coersum");
		Patient p = dsvcbean.createPatient(d.getId(), "ko", "pat");
		
		for (int i = 0; i < 10; i++) {
			psvcbean.createMedicalEntry(p.getId(), "asdasdasd"+i, CloudentUtils.MedEntryAlertType.LOW.getValue());	
		}
		
		d = dbean.getDentist("arilou");
		Vector<Patient> pts = (Vector<Patient>)d.getPatientList();
		p = pts.get(0);
		assertEquals(10, p.getMedicalhistory().getEntries().size());
		dbean.deleteDentists();
	}
}
package com.mpdmal.cloudental.tdd.tests.bean;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Set;
import java.util.Vector;
import org.junit.Test;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tdd.base.CDentAbstractBeanTest;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

public class ServicesTests extends CDentAbstractBeanTest {
	
	@Override
	public void initTestEnv() {
		_dbean.createDentist("Demo Dentist", "demo", "Demopoulos", "Demis");
		_dsvcbean.createPatient("Demo Dentist", "Kostas", "Patakas");
	}
	
	@Override
	public void closeTestEnv() {
		_dbean.deleteDentist("Demo Dentist");
		assertEquals(0, _dsvcbean.countDiscounts());
		assertEquals(0, _dsvcbean.countPricelistItems());
		assertEquals(0, _dsvcbean.countNotes());
	}
	
	@Test
	public void testDentistBean() {
		//exists
		assertEquals(null, _dbean.getDentist("Noone")); 
		
		//get
		Dentist d = _dbean.getDentist("Demo Dentist"); 
		assertEquals("Demis", d.getName());
		assertEquals("Demopoulos", d.getSurname());
		assertEquals("demo", d.getPassword());

		//update
		d.setName("Altered!");
		_dbean.updateDentist(d);
		assertEquals("Altered!", _dbean.getDentist("Demo Dentist").getName());

		//delete
		_dbean.deleteDentist(d.getUsername()); 
		assertEquals(0, _dbean.countDentists());
		
		//mass delete
		_dbean.createDentist("Arilou1", "admin", "Azarias", "Dimitris");  
		_dbean.createDentist("Arilou2", "admin", "Azarias", "Dimitris");  
		_dbean.createDentist("Arilou3", "admin", "Azarias", "Dimitris"); 
		assertEquals(3, _dbean.countDentists());
		_dbean.deleteDentists();
		assertEquals(0, _dbean.countDentists());
		
		_dbean.createDentist("Arilou1", "admin", "Azarias", "Dimitris");  
		_dbean.createDentist("Arilou2", "admin", "Azarias", "Dimitris");  
		_dbean.createDentist("Arilou3", "admin", "Azarias", "Dimitris");
		
		//mass get
		Vector<Dentist> dentists = _dbean.getDentists();
		assertEquals(3, dentists.size());
		String unms = "";
		for (Dentist dentist : dentists) {
			unms += dentist.getUsername();
		}
		assertEquals(true, unms.indexOf("Arilou1") >= 0);
		assertEquals(true, unms.indexOf("Arilou2") >= 0);
		assertEquals(true, unms.indexOf("Arilou3") >= 0);
		
		_dbean.deleteDentist("Arilou1");
		_dbean.deleteDentist("Arilou2");
		_dbean.deleteDentist("Arilou3");
	}

	@Test
	public void testLoginBean() {
		//wrong user
		assertEquals(null, _lbean.doLogin("", ""));
		//wrong pwd
		assertEquals(null, _lbean.doLogin("Demo Dentist", ""));
		//success
		assertEquals("Demis", _lbean.doLogin("Demo Dentist", "demo").getName());
	}
	@Test
	public void testDentistServicesBean() throws InvalidPostitAlertException {
		Dentist d = _dbean.getDentist("Demo Dentist");
		Set<Patient> patients = d.getPatients();
		assertEquals(false, patients.isEmpty());
		assertEquals(1, patients.size());
		assertEquals("Kostas", ((Patient)patients.toArray()[0]).getName());
		assertEquals("Patakas", ((Patient)patients.toArray()[0]).getSurname());
		
		
		try {
			_dsvcbean.createNote(d.getUsername(), "a note to warn!", CloudentUtils.PostitAlertType.ALARM.getValue());
			Thread.sleep(100);
			_dsvcbean.createNote(d.getUsername(), "a TODO note ", CloudentUtils.PostitAlertType.TODO.getValue());
			Thread.sleep(100);
			_dsvcbean.createNote(d.getUsername(), "just a note", CloudentUtils.PostitAlertType.NOTE.getValue());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		_dsvcbean.createDiscount(d.getUsername(), "Discount 1", "A friendly discount", 0.1);
		_dsvcbean.createDiscount(d.getUsername(), "Discount 2", "A friendlier discount", 0.3);
		_dsvcbean.createDiscount(d.getUsername(), "Discount 3", "A *** discount", 0.7);
		
		_dsvcbean.createPricelistItem(d.getUsername(), "Item 1", "An item", 100.0);
		_dsvcbean.createPricelistItem(d.getUsername(), "Item 2", "Another item", 400.777);
		_dsvcbean.createPricelistItem(d.getUsername(), "Item 3", "final item", 550.50);
	}
	
	@Test
	public void testPatientServicesBean() throws Exception {
		PricelistItem item = _dsvcbean.createPricelistItem("Demo Dentist", "demo item", "", 100.0);
		Discount ds = _dsvcbean.createDiscount("Demo Dentist", "demo discount", "", 0.30);

		Vector<Patient> ptnts = _psvcbean.getPatients("Demo Dentist");
		int pid = ptnts.elementAt(0).getId();
		
		Date now = new Date();
		Date future1 = new Date (System.currentTimeMillis()+1000000);
		Date future2 = new Date (System.currentTimeMillis()+5000000);
		Date past1 = new Date (System.currentTimeMillis()-1000000);
		Date past2 = new Date (System.currentTimeMillis()-5000000);
		
		
		//create activity
		_psvcbean.createActivity(pid, "test activity", now, future1, item, ds);
		_psvcbean.createActivity(pid, "another test activity", now, future2, item,ds); 
		_psvcbean.createActivity(pid, "final test activity", past1, now, item, ds);
		
		//get patient activities
		assertEquals(3, _psvcbean.getActivities(pid).size());
		//get activities by date
		assertEquals(2,_psvcbean.getActivitiesByDate(pid,past1,future1).size());
		assertEquals(3,_psvcbean.getActivitiesByDate(pid,past1,future2).size());
		assertEquals(0,_psvcbean.getActivitiesByDate(pid,past2,past1).size());
		assertEquals(1,_psvcbean.getActivitiesByDate(pid,past1,now).size());
		
		assertEquals("test activity",_psvcbean.getActivitiesByDate(pid, past1, future1).elementAt(0).getDescription());
		assertEquals("final test activity",_psvcbean.getActivitiesByDate(pid, past1, now).elementAt(0).getDescription());

		//delete patient activities
		_psvcbean.deleteActivities(ptnts.elementAt(0).getId());
		assertEquals(0, _psvcbean.getActivities(pid).size());
	}

		
}


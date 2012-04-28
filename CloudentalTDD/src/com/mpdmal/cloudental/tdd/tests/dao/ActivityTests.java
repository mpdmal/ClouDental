package com.mpdmal.cloudental.tdd.tests.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Vector;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.exception.PatientExistsException;

import static org.junit.Assert.assertEquals;

public class ActivityTests extends CDentAbstractDaoTest {
	Dentist d = new Dentist();
	Patient p = new Patient();
	Patienthistory ph = new Patienthistory();
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
				
		ph.setComments("test patient history for patient"+p.getSurname());
		ph.setStartdate(tstamp);
				
		ph.setPatient(p);
		p.setDentalhistory(ph);
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
	public void testActivities() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp past= new Timestamp(System.currentTimeMillis()-100000000);
		Timestamp past_long= new Timestamp(System.currentTimeMillis()-900000000);
		Timestamp past_short = new Timestamp(System.currentTimeMillis()-10000000);
		Timestamp future = new Timestamp(System.currentTimeMillis()+100000000);
		Timestamp future_long = new Timestamp(System.currentTimeMillis()+900000000);
		Timestamp future_short = new Timestamp(System.currentTimeMillis()+10000000);
		
		//count
		assertEquals(0, _acdao.countActivities());
		
		//create
		Activity activity = new Activity();
		activity.setDescription("an activity");
		activity.setStartdate(future_short);
		activity.setEnddate(future_long);

		PricelistItem item  = new PricelistItem();
		item.setDentist(d);
		item.setDescription("");
		item.setPrice(BigDecimal.valueOf(10.0));
		item.setTitle("pl item");
		_pcdao.updateCreate(item, false);
		
		Discount ds = new Discount();
		ds.setDentist(d);
		ds.setDescription("");
		ds.setDiscount(BigDecimal.valueOf(.10));
		ds.setTitle("discount");
		_discountdao.updateCreate(ds, false);
		
		activity.setPriceable(item);
		activity.setDiscount(ds);
		ph.addActivity(activity);
		
		d.addDiscount(ds);
		d.addPricelistItem(item);
		_acdao.updateCreate(activity, false);
		
		assertEquals(1, _acdao.countActivities());

		//get
		Vector<Activity> acts = _acdao.getActivities();
		assertEquals(1, acts.size());
		
		acts = _acdao.getActivities(p.getId());
		assertEquals(1, acts.size());
		
		acts = _acdao.getActivities(p.getId(), past, future);
		assertEquals(0, acts.size());

		acts = _acdao.getActivities(p.getId(), now, future_long);
		assertEquals(1, acts.size());

		acts = _acdao.getActivities(p.getId(), past_long, past_short);
		assertEquals(0, acts.size());

		acts = _acdao.getActivities(p.getId(), future_short, future_long);
		assertEquals(1, acts.size());

		//update
		Activity ac = acts.elementAt(0);
		ac.setDescription("Altered");
		_acdao.updateCreate(ac, true);
		
		acts = _acdao.getActivities(p.getId(), future_short, future_long);
		assertEquals(1, acts.size());
		assertEquals("Altered", acts.elementAt(0).getDescription());
	}
	
	@Override
	public void closeTestEnv() {
		_dentistdao.delete(_dentistdao.getDentist("testDentist"));
//		assertEquals(2, _dentistdao.countDentists());
//		assertEquals(0, _vdao.countVisits());
	}
}

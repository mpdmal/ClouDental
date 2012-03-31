package com.mpdmal.cloudental.tdd.tests.dao;

import java.sql.Timestamp;
import java.util.Vector;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;

public class VisitTests extends CDentAbstractDaoTest {
	Timestamp now = new Timestamp(System.currentTimeMillis());
	Timestamp past= new Timestamp(System.currentTimeMillis()-100000000);
	Timestamp past_long= new Timestamp(System.currentTimeMillis()-900000000);
	Timestamp past_short = new Timestamp(System.currentTimeMillis()-10000000);
	Timestamp future = new Timestamp(System.currentTimeMillis()+100000000);
	Timestamp future_long = new Timestamp(System.currentTimeMillis()+900000000);
	Timestamp future_short = new Timestamp(System.currentTimeMillis()+10000000);
	Activity activity = new Activity(),
			activity2 = new Activity();
		
	@Override
	public void initTestEnv() {
		Dentist d = new Dentist();
		d.setName("a");
		d.setSurname("b");
		d.setUsername("c");
		d.setPassword("e");
		
		Patient p = new Patient();
		p.setCreated(now);
		p.setName("p");
		p.setSurname("pp");
		
		Patienthistory ph = new Patienthistory();
		ph.setComments("");
		ph.setStartdate(now);
				
		activity.setDescription("an activity");
		activity.setStartdate(future_short);
		activity.setEnddate(future_long);

		activity2.setDescription("an activity2");
		activity2.setStartdate(now);
		activity2.setEnddate(future_long);

		ph.addActivity(activity);
		ph.addActivity(activity2);
		p.setDentalhistory(ph);
		d.addPatient(p);
		_dentistdao.updateCreate(d, false);
	}
	
	@Test
	public void testVisits() {
		//count
		assertEquals(0, _vdao.countPatientVisits(0));
		assertEquals(0, _vdao.countVisits());
		
		//create
		Visit v = new Visit();
		v.setComments("a visit for acitvity "+activity.getDescription());
		v.setVisitdate(now);
		v.setEnddate(future_long);
		v.setActivity(activity);

		_vdao.updateCreate(v, false);
		assertEquals(1, _vdao.countVisits());
		assertEquals(1, _vdao.countPatientVisits(activity.getPatienthistory().getPatient().getId()));
		
		//2activities 1visit each
		Visit v2 = new Visit();
		v2.setComments("a visit for activity "+activity2.getDescription());
		v2.setVisitdate(now);
		v2.setEnddate(future_short);
		activity2.addVisit(v2);
		_vdao.updateCreate(v2, false);
		
		//counts
		assertEquals(2, _vdao.countVisits());
		assertEquals(2, _vdao.countPatientVisits(activity.getPatienthistory().getPatient().getId()));
		assertEquals(1, _vdao.countActivityVisits(activity.getId()));
		assertEquals(1, _vdao.countActivityVisits(activity2.getId()));
		
		//gets
		assertEquals(2, _vdao.getVisits().size());
		assertEquals(2, _vdao.getPatientVisits(activity.getPatienthistory().getPatient().getId()).size());
		assertEquals(1, _vdao.getActivityVisits(activity.getId()).size());
		assertEquals(1, _vdao.getActivityVisits(activity2.getId()).size());
		assertEquals("a visit for acitvity "+activity.getDescription(), _vdao.getActivityVisits(activity.getId()).elementAt(0).getComments());
		
		assertEquals(0,
				_vdao.getPatientVisits(activity.getPatienthistory().getPatient().getId()
						,past_long, past_short).size());
		assertEquals(0,
				_vdao.getActivityVisits(activity.getId()
						,past_long, past_short).size());
		assertEquals(1,
				_vdao.getActivityVisits(activity.getId()
						,now, future_long).size());

		//2visit for activity 2
		Visit v3 = new Visit();
		v3.setComments("a visit for activity "+activity2.getDescription());
		v3.setVisitdate(future_short);
		v3.setEnddate(future_long);
		v3.setActivity(activity2);
		_vdao.updateCreate(v3, false);

		Vector<Visit> visits = _vdao.getActivityVisits(activity2.getId());
		assertEquals(2, visits.size());
		//delete per visit
		_vdao.delete(v);
		assertEquals(2, _vdao.countVisits());
		assertEquals(2, _vdao.countPatientVisits(activity.getPatienthistory().getPatient().getId()));
		assertEquals(0, _vdao.countActivityVisits(activity.getId()));
		assertEquals(2, _vdao.countActivityVisits(activity2.getId()));
		
		_vdao.delete(v2);
		assertEquals(1, _vdao.countVisits());
		assertEquals(1, _vdao.countPatientVisits(activity.getPatienthistory().getPatient().getId()));
		assertEquals(0, _vdao.countActivityVisits(activity.getId()));
		assertEquals(1, _vdao.countActivityVisits(activity2.getId()));
		
		_vdao.delete(v3);
		assertEquals(0, _vdao.countPatientVisits(activity.getPatienthistory().getPatient().getId()));
		assertEquals(0, _vdao.countVisits());
	}
	
	@Override
	public void closeTestEnv() {
		_dentistdao.delete(_dentistdao.getDentist("c"));
//		assertEquals(2, _dentistdao.countDentists());
//		assertEquals(0, _vdao.countVisits());
	}
}

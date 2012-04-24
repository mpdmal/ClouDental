package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Date;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Postit;
import com.mpdmal.cloudental.entities.PostitPK;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

import static org.junit.Assert.assertEquals;

public class PostitTests  extends CDentAbstractDaoTest {
	@Before
	public void initTestEnv() {
		Dentist _aDentist = new Dentist();
		_aDentist.setUsername("Arilou");
		_aDentist.setPassword("admin");
		_aDentist.setName("Dim");
		_aDentist.setSurname("Aza");
		_dentistdao.updateCreate(_aDentist, false);
		
		Dentist _aDentist2 = new Dentist();
		_aDentist2.setUsername("Mika");
		_aDentist2.setPassword("admin");
		_aDentist2.setName("Dim");
		_aDentist2.setSurname("Aza");
		_dentistdao.updateCreate(_aDentist2, false);

		assertEquals(2, _dentistdao.countDentists());
	}
	
	@Test
	public void testPostit() {
		Dentist d = _dentistdao.getDentist("Arilou");
		Postit p = new Postit();
		try {
			p.setAlert(CloudentUtils.PostitAlertType.ALARM.getValue());
		} catch (InvalidPostitAlertException e) {
			e.printStackTrace();
		}
		p.setDentist(d);
		p.setPost("test post");
		
		PostitPK id = new PostitPK();
		id.setId(d.getUsername());
		id.setPostdate(new Date());
		p.setId(id);
		
		d.addNote(p);
		_postitdao.updateCreate(p, false);
		assertEquals(1, _postitdao.countPostits(d.getUsername()));
		_postitdao.delete(p);
		assertEquals(0, _postitdao.countPostits(d.getUsername()));
		
		d.addNote(p);
		_dentistdao.updateCreate(d, true);
		assertEquals(1, _postitdao.countPostits(d.getUsername()));
		
//		assertEquals(4, _postitdao.countPostits());
//		Vector<Postit> notes = _postitdao.getPostits(_d.getUsername());
//		_postitdao.delete(notes.elementAt(1));
//		_postitdao.delete(notes.elementAt(2));
//		Postit note = notes.elementAt(0);
//		note.setPost("altered!");
//		_postitdao.updateCreate(note, true);
		
//		assertEquals(1, _postitdao.countPostits(_d.getUsername()));
//		assertEquals("altered!",_postitdao.getPostits(_d.getUsername()).elementAt(0).getPost());
		
		
//		_postitdao.delete(notes.elementAt(0));
//		assertEquals(0, _postitdao.countPostits(_d.getUsername()));
//		notes = _postitdao.getPostits();
//		_postitdao.delete(notes.elementAt(0));
//		assertEquals(0, _postitdao.countPostits());
//		
//		_dentistdao.delete(_d);
//		_dentistdao.delete(_d2);
//		assertEquals(0, _dentistdao.countDentists());
	}
	
	@Override
	public void closeTestEnv() {
		Vector<Dentist> dentists = _dentistdao.getDentists();
		for (Dentist dentist : dentists) {
			_dentistdao.delete(dentist);	//cascade delete
		}
		assertEquals(0, _dentistdao.countDentists());
		assertEquals(0, _postitdao.countPostits());
	}
}

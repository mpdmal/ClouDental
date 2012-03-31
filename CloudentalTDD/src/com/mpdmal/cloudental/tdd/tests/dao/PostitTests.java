package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Postit;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import static org.junit.Assert.assertEquals;

public class PostitTests  extends CDentAbstractDaoTest {
	Dentist _d = new Dentist(), _d2 = new Dentist();
	@Before
	public void initTestEnv() {
		assertEquals(0, _dentistdao.countDentists());
	}
	
	@Test
	public void testPostit() {
		assertEquals(3, _postitdao.countPostits(_d.getUsername()));
		assertEquals(4, _postitdao.countPostits());
		Vector<Postit> notes = _postitdao.getPostits(_d.getUsername());
		_postitdao.delete(notes.elementAt(1));
		_postitdao.delete(notes.elementAt(2));
		Postit note = notes.elementAt(0);
		note.setPost("altered!");
		_postitdao.updateCreate(note, true);
		
		assertEquals(1, _postitdao.countPostits(_d.getUsername()));
		assertEquals("altered!",_postitdao.getPostits(_d.getUsername()).elementAt(0).getPost());
		
		
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
}

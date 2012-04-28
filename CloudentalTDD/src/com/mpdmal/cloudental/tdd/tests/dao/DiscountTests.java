package com.mpdmal.cloudental.tdd.tests.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;

public class DiscountTests extends CDentAbstractDaoTest {
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
	public void testDiscounts() {
		Dentist d = _dentistdao.getDentist("Arilou");
		Discount ds = new Discount();
		ds.setDescription("Friendly discount");
		ds.setDiscount(BigDecimal.valueOf(0.2));
		ds.setTitle("asdsad");
		ds.setDentist(d);
		d.addDiscount(ds);
		_discountdao.updateCreate(ds, false);
		assertEquals(1, _discountdao.countDiscounts(d.getUsername()));
		_discountdao.delete(ds);
		assertEquals(0, _discountdao.countDiscounts(d.getUsername()));
		
		d.addDiscount(ds);
		_dentistdao.updateCreate(d, true);
		assertEquals(1, _discountdao.countDiscounts(d.getUsername()));
	}
	
	@Override
	public void closeTestEnv() {
		Vector<Dentist> dentists = _dentistdao.getDentists();
		for (Dentist dentist : dentists) {
			_dentistdao.delete(dentist);	//cascade delete
		}
		assertEquals(0, _dentistdao.countDentists());
		assertEquals(0, _discountdao.countDiscounts());
	}

}

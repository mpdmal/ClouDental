package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;

public class PricelistTest extends ArquillianCloudentTest {
	@Test 
	@InSequence (1)
	public void create() {}
	
	@Test
	@InSequence (2)
	public void getAndCount () {}
	
	@Test 
	@InSequence (3)
	public void update() {}
	
	@Test
	@InSequence (4)
	public void delete() {}
}

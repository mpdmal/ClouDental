package com.mpdmal.cloudental.tdd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mpdmal.cloudental.tdd.tests.bean.DentistServicesTests;
import com.mpdmal.cloudental.tdd.tests.bean.DentistTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DentistTests.class,
	DentistServicesTests.class,
})

public class TestSuite {
 
}
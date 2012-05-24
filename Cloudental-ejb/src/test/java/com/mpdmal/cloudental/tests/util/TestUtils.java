package com.mpdmal.cloudental.tests.util;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class TestUtils {
	
    public static Archive<?> createBasicTestDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
            .addPackage("com.mpdmal.cloudental")
            .addPackage("com.mpdmal.cloudental.beans")
            .addPackage("com.mpdmal.cloudental.beans.base")
            .addPackage("com.mpdmal.cloudental.base")
            .addPackage("com.mpdmal.cloudental.entities")
            .addPackage("com.mpdmal.cloudental.entities.base")
            .addPackage("com.mpdmal.cloudental.dao")
            .addPackage("com.mpdmal.cloudental.dao.base")
            .addPackage("com.mpdmal.cloudental.util")
            .addPackage("com.mpdmal.cloudental.util.exception")
            .addPackage("com.mpdmal.cloudental.util.exception.base")
            .addPackage("com.mpdmal.cloudental.tests.services")
            .addPackage("com.mpdmal.cloudental.tests.base")
            .addPackage("com.mpdmal.cloudental.tests.util")
            .addAsManifestResource("test-persistence.xml", "persistence.xml")
            .addAsManifestResource("ejb-jar.xml")
            .addAsManifestResource("sun-ejb-jar.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
//        System.out.println(jar.toString(true));
        return jar;
    }
    
    public static Archive<?> createAlternateTestDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
        return jar;
    }
}

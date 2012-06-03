package com.mpdmal.cloudental.tests.util;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import com.mpdmal.cloudental.tests.util.hack.TestServlet;

public class TestUtils {
	
    public static Archive<?> createBasicTestDeployment() {
		MavenDependencyResolver resolver = 
				DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");		
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class,"test.jar")
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
            .addAsManifestResource("test-logback.xml","logback.xml")
            .addAsManifestResource("ejb-jar.xml")
            .addAsManifestResource("sun-ejb-jar.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(TestServlet.class);
        
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "eartest.ear")
                .addAsModule(jar)
                .addAsModule(war)
                .addAsLibraries(resolver.artifact("org.slf4j:slf4j-api").resolveAsFiles())
                .addAsLibraries(resolver.artifact("ch.qos.logback:logback-core").resolveAsFiles())
                .addAsLibraries(resolver.artifact("ch.qos.logback:logback-classic").resolveAsFiles())        
                .setApplicationXML("application.xml");
        
//        System.out.println(war.toString(true));
//        System.out.println(jar.toString(true));
//        System.out.println(ear.toString(true));
        return ear;
    }
    
    public static Archive<?> createAlternateTestDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
        return jar;
    }
}

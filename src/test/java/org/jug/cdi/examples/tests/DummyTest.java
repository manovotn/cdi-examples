package org.jug.cdi.examples.tests;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.junit.jupiter.api.Test;

public class DummyTest {

    @Test
    public void testSomething() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance()
                .disableDiscovery() // do no scan classpath for all beans
                .addBeanClasses(GreetingBean.class); // add a bean to the synthetic bean archive
        try (SeContainer cdiContainer = seContainerInitializer.initialize()) {
            // use dynamic resolution to select a bean and invoke its method
            cdiContainer.select(GreetingBean.class).get().sayHello();
        }
    }
}

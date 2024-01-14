package org.jug.cdi.examples;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance()
                .disableDiscovery() // do not scan whole classpath for all beans
                .addBeanClasses(GreetingBean.class); // add a bean to the synthetic bean archive
        try (SeContainer cdiContainer = seContainerInitializer.initialize()) {
            // use dynamic resolution to select a bean and invoke its method
            cdiContainer.select(GreetingBean.class).get().sayHello();
        }
    }
}
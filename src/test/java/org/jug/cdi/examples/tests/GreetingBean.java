package org.jug.cdi.examples.tests;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingBean {

    public void sayHello() {
        System.out.println("Hello world!");
    }
}

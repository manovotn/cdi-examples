package org.jug.cdi.examples.injection.alternatives;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BasicFoo implements FooInterface {

    @Override
    public String ping() {
        return BasicFoo.class.getSimpleName();
    }

}

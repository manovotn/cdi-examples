package org.jug.cdi.examples.extensions;

import jakarta.enterprise.context.Dependent;

@Dependent
public class Foo {

    public String ping() {
        return Foo.class.getSimpleName();
    }
}

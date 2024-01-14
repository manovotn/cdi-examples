package org.jug.cdi.examples.injection.alternatives;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import jakarta.interceptor.Interceptor;

@Dependent // scope can differ from original bean
@Alternative
@Priority(Interceptor.Priority.APPLICATION + 2)
public class AnotherAlternativeFoo extends BasicFoo {

    @Override
    public String ping() {
        return AnotherAlternativeFoo.class.getSimpleName();
    }
}

package org.jug.cdi.examples.injection.contexts;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

@Dependent
public class DependentBean {

    private Class<?> injectedFrom;

    @Inject
    public DependentBean(InjectionPoint injectionPoint) {
        // getBean() returns null for injection done outside of beans
        injectedFrom = injectionPoint.getBean() == null ?
                DependentBean.class : injectionPoint.getBean().getBeanClass();
    }

    public Class<?> getInjectedFrom() {
        return injectedFrom;
    }
}

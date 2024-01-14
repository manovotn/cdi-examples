package org.jug.cdi.examples.injection.contexts;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SingletonBean {

    @Inject
    private DependentBean dependentBean;

    public Class<?> pingDependentBean() {
        return dependentBean.getInjectedFrom();
    }
}

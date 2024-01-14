package org.jug.cdi.examples.injection.contexts;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class RequestScopedBean {

    @Inject
    private DependentBean dependentBean;

    public Class<?> pingDependentBean() {
        return dependentBean.getInjectedFrom();
    }
}

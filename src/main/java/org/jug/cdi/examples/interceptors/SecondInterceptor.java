package org.jug.cdi.examples.interceptors;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Intercepted;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Second
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 2)
public class SecondInterceptor {

    @Inject
    @Intercepted
    Bean<?> bean;

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        // perform work prior to intercepted method invocation
        if (bean.getBeanClass().equals(InterceptedBean.class)) {
            InterceptedBean.orderOfInvocations.add(SecondInterceptor.class);
        }

        // all interceptors have to call InvocationContext#proceed()
        Object result = ic.proceed();

        // perform work after intercepted method invocation
        if (bean.getBeanClass().equals(InterceptedBean.class)) {
            InterceptedBean.orderOfInvocations.add(SecondInterceptor.class);
        }
        return result;
    }
}

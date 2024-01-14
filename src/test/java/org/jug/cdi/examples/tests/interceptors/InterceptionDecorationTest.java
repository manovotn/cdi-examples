package org.jug.cdi.examples.tests.interceptors;

import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;
import org.jug.cdi.examples.interceptors.BeanInterface;
import org.jug.cdi.examples.interceptors.FirstInterceptor;
import org.jug.cdi.examples.interceptors.InterceptedBean;
import org.jug.cdi.examples.interceptors.SecondInterceptor;
import org.jug.cdi.examples.interceptors.decorators.BeanDecorator;
import org.jug.cdi.examples.interceptors.decorators.DecoratedBean;
import org.jug.cdi.examples.interceptors.decorators.DecoratedButSkippedBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@EnableAutoWeld
@AddPackages(value = InterceptedBean.class, recursively = true)
public class InterceptionDecorationTest {

    @Inject
    BeanInterface bean;

    @Test
    public void testInterception() {
        InterceptedBean.orderOfInvocations.clear();
        bean.ping();
        List<Class<?>> expectedOrder = List.of(FirstInterceptor.class, InterceptedBean.class, FirstInterceptor.class);
        Assertions.assertEquals(expectedOrder, InterceptedBean.orderOfInvocations);

        InterceptedBean.orderOfInvocations.clear();
        bean.pong();
        expectedOrder = List.of(FirstInterceptor.class, SecondInterceptor.class, InterceptedBean.class, SecondInterceptor.class, FirstInterceptor.class);
        Assertions.assertEquals(expectedOrder, InterceptedBean.orderOfInvocations);
    }

    @Inject
    @Important
    DecoratedBean decoratedBean;

    @Inject
    @Important
    @Fast
    DecoratedButSkippedBean decoratedButSkippedBean;

    @Test
    public void testDecoration() {
        Assertions.assertEquals(DecoratedBean.class.getSimpleName() + BeanDecorator.class.getSimpleName(), decoratedBean.ping());
        Assertions.assertEquals(DecoratedBean.class.getSimpleName(), decoratedBean.pong());

        Assertions.assertEquals(DecoratedButSkippedBean.class.getSimpleName(), decoratedButSkippedBean.ping());
        Assertions.assertEquals(DecoratedButSkippedBean.class.getSimpleName(), decoratedButSkippedBean.pong());
    }
}

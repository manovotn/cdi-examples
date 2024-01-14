package org.jug.cdi.examples.tests.injection;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.jug.cdi.examples.injection.contexts.DependentBean;
import org.jug.cdi.examples.injection.contexts.RequestScopedBean;
import org.jug.cdi.examples.injection.contexts.SingletonBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@EnableAutoWeld
@AddBeanClasses(SingletonBean.class) // @Singleton isn't a bean defining annotation
public class BeanContextTest {

    @Inject
    RequestScopedBean requestScopedBean;

    // allows to manually activate req. context
    @Inject
    RequestContextController contextController;

    @Inject
    DependentBean dependentBean;

    @Inject
    SingletonBean singletonBean;

    @Test
    public void testContexts() {
        // req. context isn't active
        Assertions.assertThrows(ContextNotActiveException.class, ()
                -> requestScopedBean.pingDependentBean());

        // manually activate and re-try
        contextController.activate();
        Assertions.assertEquals(RequestScopedBean.class, requestScopedBean.pingDependentBean());
        Assertions.assertEquals(SingletonBean.class, singletonBean.pingDependentBean());
        Assertions.assertEquals(DependentBean.class, dependentBean.getInjectedFrom());

        // deactivate req. context; not needed for the sake of this test
        contextController.deactivate();
    }
}

package org.jug.cdi.examples.tests.injection;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;
import org.jug.cdi.examples.injection.beans.BeanProducer;
import org.jug.cdi.examples.injection.beans.ManagedBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@EnableAutoWeld
@AddPackages(BeanProducer.class)
public class BeanDefinitionTest {

    @Inject
    @Any
    Instance<Object> instance;

    @Test
    public void assertBeansInDeployment() {
        // assert producer bean and managed are available
        Assertions.assertTrue(instance.select(BeanProducer.class, Default.Literal.INSTANCE).isResolvable());
        Assertions.assertTrue(instance.select(ManagedBean.class, Fast.Literal.INSTANCE).isResolvable());

        // assert class with inappropriate c-tor and inner class are not CDI beans
        Assertions.assertFalse(instance.select(ManagedBean.NotaBean.class, Default.Literal.INSTANCE).isResolvable());
        Assertions.assertFalse(instance.select(ManagedBean.ProducedBean.class, Default.Literal.INSTANCE).isResolvable());

        // assert ProducedBean exists as a bean with @Important qualifier; also check in the Integer bean
        Assertions.assertTrue(instance.select(ManagedBean.ProducedBean.class, Important.Literal.INSTANCE).isResolvable());
        Assertions.assertTrue(instance.select(Integer.class, Important.Literal.INSTANCE).isResolvable());
    }
}

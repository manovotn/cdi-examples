package org.jug.cdi.examples.injection.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;

// producers have to be inside CDI beans
@ApplicationScoped
public class BeanProducer {

    @Produces
    @Important
    @Dependent
    Integer meaningOfLife = 42;

    @Produces
    @Important
    ManagedBean.ProducedBean createBean(@Fast ManagedBean managedBean) {
        return new ManagedBean.ProducedBean(managedBean.new NotaBean());
    }
}

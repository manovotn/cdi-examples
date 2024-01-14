package org.jug.cdi.examples.interceptors.decorators;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Decorated;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;
import org.jug.cdi.examples.interceptors.BeanInterface;

@Decorator
@Priority(1)
public class BeanDecorator implements BeanInterface {

    @Inject
    @Delegate
    @Important // allows to filter which beans of this type are decorated
    // can use built-in @Any qualifier to catch all the implementations
    BeanInterface delegate;

    @Inject
    @Decorated
    Bean<BeanInterface> decoratedBean;

    @Override
    public String ping() {
        // skip if the bean is also annotated @Fast
        if (decoratedBean.getQualifiers().contains(Fast.Literal.INSTANCE)) {
            return delegate.ping();
        }
        // perform some decoration otherwise
        return delegate.ping() + BeanDecorator.class.getSimpleName();
    }

    @Override
    public String pong() {
        // noop, just delegate
        return delegate.pong();
    }
}

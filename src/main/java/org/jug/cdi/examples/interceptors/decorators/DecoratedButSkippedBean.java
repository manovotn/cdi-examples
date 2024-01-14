package org.jug.cdi.examples.interceptors.decorators;

import jakarta.enterprise.context.ApplicationScoped;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;
import org.jug.cdi.examples.interceptors.BeanInterface;

@ApplicationScoped
@Important
@Fast
public class DecoratedButSkippedBean implements BeanInterface {

    @Override
    public String ping() {
        return DecoratedButSkippedBean.class.getSimpleName();
    }

    @Override
    public String pong() {
        return DecoratedButSkippedBean.class.getSimpleName();
    }
}

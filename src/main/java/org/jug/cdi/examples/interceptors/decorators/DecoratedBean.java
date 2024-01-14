package org.jug.cdi.examples.interceptors.decorators;

import jakarta.enterprise.context.ApplicationScoped;
import org.jug.cdi.examples.common.Important;
import org.jug.cdi.examples.interceptors.BeanInterface;

@ApplicationScoped
@Important
public class DecoratedBean implements BeanInterface {

    @Override
    public String ping() {
        return DecoratedBean.class.getSimpleName();
    }

    @Override
    public String pong() {
        return DecoratedBean.class.getSimpleName();
    }
}

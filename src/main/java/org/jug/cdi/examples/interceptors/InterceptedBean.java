package org.jug.cdi.examples.interceptors;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@First
public class InterceptedBean implements BeanInterface {

    public static List<Class<?>> orderOfInvocations = new ArrayList<>();

    public String ping() {
        orderOfInvocations.add(InterceptedBean.class);
        return "ping";
    }

    @Second
    public String pong() {
        orderOfInvocations.add(InterceptedBean.class);
        return "pong";
    }
}

package org.jug.cdi.examples.injection.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;

@ApplicationScoped //at most one scope
@Fast // any amount of qualifiers
public class ManagedBean {

    @Inject // this c-tor will be injected into
    public ManagedBean(@Important ProducedBean bean) {
    }

    // no-args is only needed for beans requiring proxyability
    public ManagedBean() {
    }

    public String ping(){
        return "foo";
    }


    // class could become a bean but has invalid constructor
    // can still become a bean if you use a producer, see BeanProducer
    @ApplicationScoped
    public static class ProducedBean {

        public ProducedBean(NotaBean notaBean) {
        }
    }

    // inner classes cannot become beans; despite the scope annotation, this class will not be recognized
    // Weld will log a WARN message
    @ApplicationScoped
    public class NotaBean {

    }

}

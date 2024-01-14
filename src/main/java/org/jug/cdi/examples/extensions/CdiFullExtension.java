package org.jug.cdi.examples.extensions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

// also known as Portable extensions
public class CdiFullExtension implements Extension {

    //Application lifecycle events, that are fired once:
    // * BeforeBeanDiscovery
    // * AfterTypeDiscovery
    // * AfterBeanDiscovery
    // * AfterDeploymentValidation
    // * BeforeShutdown

    // Bean discovery events, that are fired multiple times:
    // * ProcessAnnotatedType
    // * ProcessInjectionPoint
    // * ProcessInjectionTarget
    // * ProcessBeanAttributes
    // * ProcessBean
    // * ProcessProducer
    // * ProcessObserverMethod

    public void processAnnotatedType(@Observes ProcessAnnotatedType<? extends BarInterface> pat) {
        // Bar bean was added by another extension and is further modified here
        pat.configureAnnotatedType().add(ApplicationScoped.Literal.INSTANCE);
    }

    private int synthObserverNotified = 0;

    public void synthObserver(@Observes AfterBeanDiscovery abd) {
        abd.addObserverMethod()
                .observedType(Integer.class)
                .notifyWith(eventContext -> synthObserverNotified++)
                .beanClass(CdiFullExtension.class)
                .addQualifier(SyntheticQualifier.Literal.INSTANCE);
    }

    public int getSynthObserverNotified() {
        return synthObserverNotified;
    }

}

package org.jug.cdi.examples.observers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Reception;

@ApplicationScoped
public class ConditionalObserver {

    public static int syncNotified = 0;

    public void observeIfExists(@Observes(notifyObserver = Reception.IF_EXISTS) Payload payload) {
        syncNotified++;
    }

    public void ping() {
    }
}

package org.jug.cdi.examples.observers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;

@RequestScoped
public class InactiveObserver {

    public static boolean observerNotified = false;

    // Observer method belonging to an inactive scope will not be notified
    public void observe(@Observes String payload) {
        observerNotified = true;
    }
}

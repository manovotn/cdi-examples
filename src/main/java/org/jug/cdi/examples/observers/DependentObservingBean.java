package org.jug.cdi.examples.observers;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@Dependent
public class DependentObservingBean {

    private boolean syncNotified = false;

    public void observe(@Observes String payload) {
        syncNotified = true;
        System.out.println(this.getClass().getSimpleName() + " received an event with payload type: " + payload.getClass().getSimpleName());
    }

    public boolean isSyncNotified() {
        return syncNotified;
    }
}

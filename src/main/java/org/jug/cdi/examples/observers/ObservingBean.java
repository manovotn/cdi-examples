package org.jug.cdi.examples.observers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.enterprise.inject.spi.EventMetadata;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

@ApplicationScoped
public class ObservingBean {

    private int syncNotified = 0;
    private int withFastQualifierNotified = 0;
    private int withImportantQualifierNotified = 0;
    private int withBothQualifiersNotified = 0;
    private int asyncNotified = 0;
    private Type eventType;
    private Set<Annotation> eventQualifiers = Collections.EMPTY_SET;

    public void observe(@Observes Payload payload) {
        syncNotified++;
    }

    public void observeWithQualifier1(@Observes @Fast Payload payload) {
        withFastQualifierNotified++;
    }

    public void observeWithQualifier2(@Observes @Important Payload payload, EventMetadata metadata) {
        // mark the type of event we received
        eventType = metadata.getType();
        withImportantQualifierNotified++;
    }

    public void observeWithQualifier3(@Observes @Important @Fast Payload payload, EventMetadata metadata) {
        // remember all the qualifiers of the event
        eventQualifiers = metadata.getQualifiers();
        withBothQualifiersNotified++;
    }

    public void observeAsync(@ObservesAsync Payload payload) throws InterruptedException {
        asyncNotified++;
    }

    public int getSyncNotified() {
        return syncNotified;
    }

    public int getWithFastQualifierNotified() {
        return withFastQualifierNotified;
    }

    public int getWithImportantQualifierNotified() {
        return withImportantQualifierNotified;
    }

    public int getWithBothQualifiersNotified() {
        return withBothQualifiersNotified;
    }

    public int getAsyncNotified() {
        return asyncNotified;
    }

    public Type getEventType() {
        return eventType;
    }

    public Set<Annotation> getEventQualifiers() {
        return eventQualifiers;
    }
}

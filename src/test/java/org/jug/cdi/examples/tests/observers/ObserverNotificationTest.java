package org.jug.cdi.examples.tests.observers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;
import org.jug.cdi.examples.observers.ConditionalObserver;
import org.jug.cdi.examples.observers.DependentObservingBean;
import org.jug.cdi.examples.observers.HeavyPayload;
import org.jug.cdi.examples.observers.ObservingBean;
import org.jug.cdi.examples.observers.Payload;
import org.jug.cdi.examples.observers.InactiveObserver;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@EnableAutoWeld
@AddBeanClasses(InactiveObserver.class)
public class ObserverNotificationTest {

    @Inject
    Event<Payload> plainEvent;

    @Inject
    @Fast
    Event<Payload> fastEvent;

    @Inject
    @Important
    Event<Payload> importantEvent;

    @Inject
    @Fast
    @Important
    Event<Payload> fastImportantEvent;

    @Test
    public void testSynchronousEvents(ObservingBean observingBean) {
        assertInitialState(observingBean);

        // event type is determined in runtime and can be a subclass of the declared type
        HeavyPayload p = new HeavyPayload();
        plainEvent.fire(p);
        fastEvent.fire(p);
        importantEvent.fire(p);
        fastImportantEvent.fire(p);

        // basic observer was notified four times as all events had the correct type and qualifiers
        assertEquals(4, observingBean.getSyncNotified());

        // async observer was not notified at all, see the test below
        assertEquals(0, observingBean.getAsyncNotified());

        // observer with @Fast qualifier was notified twice and so was observer with @Important qualifier
        assertEquals(2, observingBean.getWithFastQualifierNotified());
        assertEquals(2, observingBean.getWithImportantQualifierNotified());

        // finally, observer with both, @Fast and @Important, got only one event
        assertEquals(1, observingBean.getWithBothQualifiersNotified());

        // check the metadata from the event, expected type should be HeavyPayload
        // and check the qualifier set
        assertEquals(HeavyPayload.class, observingBean.getEventType());
        assertEquals(Set.of(Fast.Literal.INSTANCE, Important.Literal.INSTANCE, Any.Literal.INSTANCE), observingBean.getEventQualifiers());
    }

    @Test
    public void testAsynchronousEvent(ObservingBean observingBean) throws ExecutionException, InterruptedException, TimeoutException {
        assertInitialState(observingBean);

        CompletionStage<Payload> payloadCompletionStage = plainEvent.fireAsync(new Payload());
        // forces this thread to wait for completion of notification
        payloadCompletionStage.toCompletableFuture().get(3l, TimeUnit.SECONDS);
        assertEquals(observingBean.getAsyncNotified(), 1);
    }

    @Test
    public void testDifferentScopes(DependentObservingBean observingBean) {
        assertFalse(observingBean.isSyncNotified());
        assertFalse(InactiveObserver.observerNotified);

        BeanManager bm = CDI.current().getBeanManager();
        bm.getEvent().select(String.class).fire("foo");

        // The state of the bean is NOT changed even though the event was fired (see terminal output).
        // Scope of observing beans matters, @Dependent behaves differently!
        assertFalse(observingBean.isSyncNotified());

        // ReqScopedBean wasn't notified either because it's context (Request context) is not active inside this test
        assertFalse(InactiveObserver.observerNotified);
    }

    @Test
    public void testConditionalObserver(ConditionalObserver observer) {
        assertEquals(0, ConditionalObserver.syncNotified);

        // fire an event without using the bean
        plainEvent.fire(new Payload());
        assertEquals(0, ConditionalObserver.syncNotified);

        // invoke any bean method to ensure it exists
        observer.ping();
        plainEvent.fire(new Payload());
        assertEquals(1, ConditionalObserver.syncNotified);
    }

    private void assertInitialState(ObservingBean observingBean) {
        assertEquals(observingBean.getAsyncNotified(), 0);
        assertEquals(observingBean.getSyncNotified(), 0);
        assertEquals(observingBean.getWithBothQualifiersNotified(), 0);
        assertEquals(observingBean.getWithImportantQualifierNotified(), 0);
        assertEquals(observingBean.getWithFastQualifierNotified(), 0);
    }
}

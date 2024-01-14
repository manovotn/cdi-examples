package org.jug.cdi.examples.tests.extensions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;
import org.jug.cdi.examples.extensions.Bar;
import org.jug.cdi.examples.extensions.CdiFullExtension;
import org.jug.cdi.examples.extensions.Foo;
import org.jug.cdi.examples.extensions.SyntheticQualifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@EnableWeld
public class ExtensionTest {

    @WeldSetup
    public static WeldInitiator initiator = WeldInitiator.of(new Weld()
            .addExtensions(CdiFullExtension.class)
            .addBeanClass(Foo.class)
            .disableDiscovery());

    @Inject
    @SyntheticQualifier
    Event<Integer> event;

    @Inject
    CdiFullExtension extension;

    @Inject
    BeanManager bm;

    @Test
    public void testExtensions() {
        // verify annotated type was added
        Instance.Handle<Bar> barHandle = bm.createInstance().select(Bar.class).getHandle();
        Assertions.assertTrue(barHandle.getBean().getScope().equals(ApplicationScoped.class));
        Assertions.assertNotNull(barHandle.get());


        // test synthetic observer works
        Assertions.assertEquals(0, extension.getSynthObserverNotified());
        event.fire(10);
        Assertions.assertEquals(1, extension.getSynthObserverNotified());

        // verify Foo bean was modified correctly
        Instance<Foo> fooInstance = bm.createInstance().select(Foo.class, SyntheticQualifier.Literal.INSTANCE, Important.Literal.INSTANCE);
        Assertions.assertTrue(fooInstance.isResolvable());

        // verify extension registered String-typed bean
        Instance<String> stringInstance = bm.createInstance().select(String.class, Fast.Literal.INSTANCE);
        Assertions.assertTrue(stringInstance.isResolvable());
        Assertions.assertEquals(stringInstance.get(), "foo42");
    }
}

package org.jug.cdi.examples.tests.injection;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.jug.cdi.examples.injection.alternatives.AlternativeFoo;
import org.jug.cdi.examples.injection.alternatives.AnotherAlternativeFoo;
import org.jug.cdi.examples.injection.alternatives.FooInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@EnableAutoWeld
@AddPackages(FooInterface.class)
public class AlternativesTest {

    @Inject
    Instance<FooInterface> fooInterfaceInstance;

    @Test
    public void testAlternatives() {
        Assertions.assertTrue(fooInterfaceInstance.isResolvable());
        // alternative with the highest priority is chosen
        Assertions.assertEquals(AnotherAlternativeFoo.class.getSimpleName(), fooInterfaceInstance.get().ping());

        // alternatives that aren't selected and aren't overridden by other alternatives can still be accessed
        AlternativeFoo alternativeFoo = fooInterfaceInstance.select(AlternativeFoo.class).get();
        Assertions.assertEquals(AlternativeFoo.class.getSimpleName(), alternativeFoo.ping());
    }
}

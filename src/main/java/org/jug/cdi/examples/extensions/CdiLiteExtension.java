package org.jug.cdi.examples.extensions;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Validation;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import org.jug.cdi.examples.common.Fast;
import org.jug.cdi.examples.common.Important;

import java.util.ArrayList;
import java.util.List;

public class CdiLiteExtension implements BuildCompatibleExtension {

    private List<String> processedBeanClasses = new ArrayList<>();

    // Entry point into type discovery; mostly for registering beans that would normally not be discovered

    @Discovery
    public void discovery(ScannedClasses classes, MetaAnnotations metaAnnotations) {
        // register a bean that wouldn't get discovered otherwise
        classes.add(Bar.class.getName());

        // register an annotation as a qualifier
        metaAnnotations.addQualifier(SyntheticQualifier.class);
    }

    // Modification of discovered types, typically removal or addition of annotations

    @Enhancement(types = Object.class, withSubtypes = true)
    public void inspectClasses(ClassInfo classInfo) {
        // modifiers, methods, fields, annotations, ...
        String name = classInfo.name();
        if (!name.contains("org.jboss.weld")) {
            processedBeanClasses.add(classInfo.name());
        }
    }

    @Enhancement(types = Foo.class)
    public void modifyClass(ClassConfig classConfig) {
        // modify the bean, add scope and qualifier
        classConfig.removeAllAnnotations();
        classConfig.addAnnotation(ApplicationScoped.class);
        classConfig.addAnnotation(Important.class);
        classConfig.addAnnotation(SyntheticQualifier.class);
    }

    // Observe registered beans and observers

    @Registration(types = Foo.class)
    public void registration(BeanInfo beanInfo) {
        // inspect bean data; accounts for all changes done in previous phases
        if (!beanInfo.scope().isNormal()) {
            throw new IllegalStateException("Foo should be a normal scoped bean!");
        }
        if (!(beanInfo.qualifiers().size() == 3)) {
            throw new IllegalStateException("Foo should have three qualifiers - @Any, @Important, @SyntheticQualifier");
        }
    }

    // Register fully synthetic beans and/or observers

    @Synthesis
    public void synthesis(SyntheticComponents components) {
        components.addBean(String.class)
                .qualifier(Fast.class)
                .scope(Dependent.class)
                .type(String.class)
                .createWith(StringBeanCreator.class)
                .withParam("meaningOfLife", 42);
    }

    // Hook for any custom validation that extensions may wish to perform

    @Validation
    public void validation(Messages messages) {
        messages.info("Following non-Weld beans were processed: " + processedBeanClasses);
    }

    public static class StringBeanCreator implements SyntheticBeanCreator<String> {

        @Override
        public String create(Instance lookup, Parameters params) {
            return "foo" + params.get("meaningOfLife", Integer.class);
        }
    }
}

package org.jug.cdi.examples.common;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Important {

    final class Literal extends AnnotationLiteral<Important> implements Important {
        public static final Literal INSTANCE = new Literal();
    }
}

package org.jug.cdi.examples.extensions;

import jakarta.enterprise.util.AnnotationLiteral;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SyntheticQualifier {
    final class Literal extends AnnotationLiteral<SyntheticQualifier> implements SyntheticQualifier {
        public static final Literal INSTANCE = new Literal();
    }
}

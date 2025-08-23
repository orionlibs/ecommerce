package de.hybris.platform.directpersistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ForceJALO
{
    public static final String CONSISTENCY_CHECK = "consistency check";
    public static final String ABSTRACT_METHOD_IMPLEMENTATION = "abstract method implementation";
    public static final String SOMETHING_ELSE = "something else";


    String reason();
}

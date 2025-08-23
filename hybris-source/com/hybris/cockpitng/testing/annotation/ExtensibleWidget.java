/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that testing widget should be extensible.
 * Every test class that extends {@link com.hybris.cockpitng.testing.AbstractCockpitngUnitTest}
 * by default consider widget as extensible
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface ExtensibleWidget
{
    int CONSTRUCTORS = 1 << 1;
    int METHODS = 1 << 2;
    int FIELDS = 1 << 3;
    int ALL = CONSTRUCTORS | METHODS | FIELDS;


    int level() default CONSTRUCTORS | FIELDS;


    boolean value() default true;


    /**
     * name of method in your testing class, that allows specify methods which are on purpose inextensible.
     * This method should has no arguments, and return List&lt;Method&gt;
     * @see com.hybris.cockpitng.testing.annotation.InextensibleMethod
     * @return
     */
    String privateMethodsProviderName() default "";
}

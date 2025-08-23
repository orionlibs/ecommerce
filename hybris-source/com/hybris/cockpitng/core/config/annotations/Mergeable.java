/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies that the marked cockpit configuration class can be merged with the same class from the parent context.
 *
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mergeable
{
    /**
     * List of key properties of the marked class. These properties will be taken as key to identify the counterpart
     * object in the parent context.
     *
     * @return key properties of the marked class
     */
    String[] key();


    /**
     * Optional name of the merge method to be used to do the merge object with its counterpart form the parent context.
     * This method must accept one parameter for the counterpart object to be passed.
     *
     * @return optional name of the merge method
     */
    String mergeMethod() default "";


    /**
     * Optional name of a property holding the merge mode. The property must contain one of these modes (or be empty
     * which defaults to "merge"):
     *
     * <code>
     * "merge"
     * "replace"
     * "remove"
     * </code>
     *
     * @return optional name of a property holding the merge mode
     */
    String mergeModeProperty() default "";
}

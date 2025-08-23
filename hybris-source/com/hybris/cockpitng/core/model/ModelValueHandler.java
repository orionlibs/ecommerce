/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

/**
 * Handles the values of a model. Provides implementation of get and set value of a model.
 */
public interface ModelValueHandler
{
    /**
     * Stores the given value to the model using the given expression. You can use expressions like 'product.media.code'
     * to navigate through data structures. See implementations for detail. It also notifies the widget about the change.
     * This is used to keep cockpit NG components like editors and actions uptodate.
     *
     * @param model
     *           the object to apply the expression on
     * @param expression
     *           the expression, can be like 'product.media.code'
     * @param value
     *           the value to store
     */
    void setValue(Object model, String expression, Object value);


    /**
     * Retrieves the value from the model using the given expression. You can use expressions like 'product.media.code'
     * to navigate through data structures. See implementations for detail.
     *
     * @param model
     *           the object to apply the expression on
     * @param expression
     *           the expression, can be like 'product.media.code'
     * @param useSessionLanguageForLocalized
     *           if true the value for the session language will be returned instead of all values for all available
     *           locales
     * @return the value of evaluation ef the given expression
     * @since 1.15.4
     */
    Object getValue(Object model, String expression, boolean useSessionLanguageForLocalized);


    /**
     * Retrieves the value from the model using the given expression. You can use expressions like 'product.media.code'
     * to navigate through data structures. See implementations for detail.
     *
     * @param model
     *           the object to apply the expression on
     * @param expression
     *           the expression, can be like 'product.media.code'
     * @return the value of evaluation ef the given expression
     */
    Object getValue(Object model, String expression);


    /**
     * Retrieves the type of the value of the model using the given expression.
     *
     * @param model
     *           the object to apply the expression on
     * @param expression
     *           the expression, can be like 'product.media.code'
     * @return the type of the value
     */
    <T> Class<T> getValueType(Object model, String expression);
}

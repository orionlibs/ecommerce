/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

import java.io.Serializable;

/**
 * The widget model holds all data related to a single widget instance.
 * It has a scope of a widget instance which basically means session scope limited to the existence of the widget
 * instance (widget instance can be created and destroyed dynamically when widget templates are used).
 * Also, the visibility of the widget model is limited to the widget instance which it belongs to.
 * Use it as a MVC model to store all data needed by the controller and view but nothing related to the view itself.
 *
 * Changes of the model are observed by widget and used to notify components like editor and actions.
 * If you change any of the objects stored in the widget model directly (by not calling
 * {@link #setValue(String, Object)} or {@link #put(String, Object)} on the widget model - which notifies the observers
 * automatically) you can call the method {@link #changed()} to trigger the notification.
 *
 * &lt;@Note the scope of the widget model is larger then of a ZK component or controller! Never store a ZK component or
 *       controller in the widget model. It only should contain data needed to render the widget view - as in the MVC
 *       pattern.&gt;
 */
public interface WidgetModel extends Serializable, Observable
{
    /**
     * Adds given data to this widget model and registers it to the given attribute name.
     *
     * @param attribute the attribute to register given object to
     * @param data the object to add
     * @return the previously put data
     */
    Object put(String attribute, Object data);


    /**
     * Removes the object registered to the given attribute name.
     *
     * @param attribute the attribute to which the data to be removed are registered
     * @return the removed object
     */
    Object remove(String attribute);


    /**
     * Stores the given value to this model using the given expression. You can use expressions like
     * 'product.media.code' to navigate through data structures. See implementations for detail.
     * It also notifies the widget about the change. This is used to keep cockpit NG components like editors and actions
     * uptodate.
     *
     * @param expression the expression, can be like 'product.media.code'
     * @param value the value to store
     */
    void setValue(String expression, Object value);


    /**
     * Retrieves the value from this model using the given expression. You can use expressions like
     * 'product.media.code' to navigate through data structures. See implementations for detail.
     *
     * @param expression the expression, can be like 'product.media.code'
     * @param valueType expected value type
     * @return the value
     */
    <T> T getValue(String expression, Class<T> valueType);


    /**
     * Retrieves the type of the value of this model using the given expression.
     *
     * @param expression the expression, can be like 'product.media.code'
     * @return the type of the value
     */
    <T> Class<T> getValueType(String expression);


    /**
     * Removes a previously registered {@link ModelObserver}.
     *
     * @param key attribute for changes of which the observer was interested
     * @param observerId id of the observer to remove. The object which represents the id should override equals method.
     */
    default void removeObserver(final String key, final Object observerId)
    {
    }


    /**
     * Removes a previously registered {@link ModelObserver}.
     *
     * @param observerId id of the observer to remove. The object which represents the id should override equals method.
     */
    default void removeObserver(final Object observerId)
    {
    }


    /**
     * Removes a previously registered {@link ModelObserver}s.
     */
    void removeAllObservers();
}

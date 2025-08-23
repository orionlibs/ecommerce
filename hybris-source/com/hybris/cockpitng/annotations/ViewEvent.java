/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method to be invoked when a zk event comes in.
 *
 * The method should have either one argument of type {@link org.zkoss.zk.ui.event.Event} or none.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface ViewEvent
{
    /**
     * The event name, e.g. {@link org.zkoss.zk.ui.event.Events}.ON_CLICK
     */
    String eventName();


    /**
     * The id of the component. If not provided, the {@link org.zkoss.zk.ui.event.EventListener} is applied to the
     * {@link com.hybris.cockpitng.components.Widgetslot}.
     */
    String componentID() default "";
}

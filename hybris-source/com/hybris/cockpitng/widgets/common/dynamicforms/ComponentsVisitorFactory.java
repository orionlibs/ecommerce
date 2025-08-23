/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.dynamicforms;

import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Abstract factory which creates instances of {@link ComponentsVisitor}
 */
public interface ComponentsVisitorFactory
{
    /**
     * Creates new visitor.
     *
     * @return new instance of {@link ComponentsVisitor}, if visitor cannot be created returns
     *         {@link ComponentsVisitor#NULL}
     */
    default ComponentsVisitor createVisitor(final String typeCode, final WidgetInstanceManager widgetInstanceManager)
    {
        if(typeCode == null)
        {
            throw new IllegalArgumentException("typeCode cannot be null");
        }
        return null;
    }
}

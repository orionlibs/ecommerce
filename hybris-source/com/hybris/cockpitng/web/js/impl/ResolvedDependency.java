/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Information about dependency, that has been found
 */
public class ResolvedDependency
{
    public static final byte POINT_HEADER = 0x10;
    public static final byte POINT_BEFORE_BODY = 0x01;
    public static final byte POINT_AFTER_BODY = 0x11;
    public static final byte POINT_BEFORE_WIDGET = 0x02;
    public static final byte POINT_AFTER_WIDGET = 0x12;
    private final DependencyDefinition definition;
    private final ResourcePath path;
    private final String type;
    private final Set<String> widgets = new HashSet<>();
    private Byte injectionPoint;


    public ResolvedDependency(final String type, final DependencyDefinition definition, final ResourcePath path)
    {
        this.type = type;
        this.definition = definition;
        this.path = path;
    }


    public DependencyDefinition getDefinition()
    {
        return definition;
    }


    public ResourcePath getPath()
    {
        return path;
    }


    public String getType()
    {
        return type;
    }


    public byte getInjectionPoint()
    {
        return injectionPoint != null ? injectionPoint : POINT_HEADER;
    }


    public boolean isInjectionPointSet()
    {
        return injectionPoint != null;
    }


    public void setInjectionPoint(final byte injectionPoint)
    {
        this.injectionPoint = injectionPoint;
    }


    public void setDefaultInjectionPoint()
    {
        this.injectionPoint = null;
    }


    public void addDependingWidget(final String widgetId)
    {
        this.widgets.add(widgetId);
    }


    public void addDependingWidgets(final Set<String> widgets)
    {
        this.widgets.addAll(widgets);
    }


    public Set<String> getDependingWidgets()
    {
        return Collections.unmodifiableSet(widgets);
    }
}

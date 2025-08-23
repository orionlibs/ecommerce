/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.ui.impl;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 * Default implementation for {@link WidgetInstance}.
 */
public class DefaultWidgetInstance implements WidgetInstance
{
    private static final long serialVersionUID = -2796626172949242705L;
    private final Widget widget;
    private final transient Object model;
    private String positionInfo;
    private int selectedChild;
    private WidgetInstance parent;
    private final int index;
    private transient Object creator;
    private WidgetInstance templateRoot;


    public DefaultWidgetInstance(final Widget widget, final Object model, final int index)
    {
        assert (widget != null);
        this.widget = widget;
        this.model = model;
        this.index = index;
    }


    @Override
    public Widget getWidget()
    {
        return widget;
    }


    @Override
    public Object getModel()
    {
        return model;
    }


    @Override
    public String getPositionInfo()
    {
        return positionInfo;
    }


    public void setPositionInfo(final String positionInfo)
    {
        this.positionInfo = positionInfo;
    }


    @Override
    public WidgetInstance getParent()
    {
        return parent;
    }


    public void setParent(final WidgetInstance parent)
    {
        this.parent = parent;
    }


    public int getIndex()
    {
        return this.index;
    }


    @Override
    public String getId()
    {
        String id = getWidget().getId();
        if(index > 0)
        {
            id += Integer.toString(index);
        }
        return id;
    }


    @Override
    public int getSelectedChildIndex()
    {
        return this.selectedChild;
    }


    @Override
    public void setSelectedChildIndex(final int selectedChild)
    {
        this.selectedChild = selectedChild;
    }


    @Override
    public boolean equals(final Object object)
    {
        if(object == null)
        {
            return false;
        }
        return (object.getClass() == this.getClass()) && (getId().equals(((WidgetInstance)object).getId()));
    }


    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }


    @Override
    public String toString()
    {
        return getWidget().getWidgetDefinitionId() + "(" + getId() + ")";
    }


    @Override
    public Object getCreator()
    {
        return creator;
    }


    public void setCreator(final Object creator)
    {
        this.creator = creator;
    }


    public void setTemplateRoot(final WidgetInstance templateRoot)
    {
        this.templateRoot = templateRoot;
    }


    @Override
    public WidgetInstance getTemplateRoot()
    {
        return templateRoot;
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import java.util.Collections;
import java.util.List;

/**
 * Result of event forwarding from template widget to its instances.
 */
public class RuleEngineResult
{
    private final List<WidgetInstance> instances;
    private final boolean updateNeeded;
    private WidgetInstance parent;
    private boolean widgetAboutToShow;
    private boolean widgetAboutToClose;


    public boolean isWidgetAboutToShow()
    {
        return widgetAboutToShow;
    }


    public void setWidgetAboutToShow(final boolean widgetAboutToShow)
    {
        this.widgetAboutToShow = widgetAboutToShow;
    }


    public boolean isWidgetAboutToClose()
    {
        return widgetAboutToClose;
    }


    public void setWidgetAboutToClose(final boolean widgetAboutToClose)
    {
        this.widgetAboutToClose = widgetAboutToClose;
    }


    public RuleEngineResult(final List<WidgetInstance> instances, final boolean updateNeeded)
    {
        this.instances = (instances == null ? Collections.<WidgetInstance>emptyList() : instances);
        this.updateNeeded = updateNeeded;
    }


    public RuleEngineResult(final List<WidgetInstance> instances, final WidgetInstance parent, final boolean updateNeeded)
    {
        this.instances = (instances == null ? Collections.<WidgetInstance>emptyList() : instances);
        this.parent = parent;
        this.updateNeeded = updateNeeded;
    }


    public WidgetInstance getParent()
    {
        return parent;
    }


    public List<WidgetInstance> getInstances()
    {
        return instances;
    }


    public boolean isUpdateNeeded()
    {
        return updateNeeded;
    }
}

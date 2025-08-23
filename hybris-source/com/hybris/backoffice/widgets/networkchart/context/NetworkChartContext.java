/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart.context;

import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Optional;

/**
 * Context used by network chart widget. Additionally to {@link DefaultContext} it stores {@link WidgetInstanceManager}
 * and information about init data for network chart.
 */
public class NetworkChartContext extends DefaultContext
{
    private final WidgetInstanceManager wim;
    private Object initData;


    public NetworkChartContext(final WidgetInstanceManager wim)
    {
        this.wim = wim;
    }


    public WidgetInstanceManager getWim()
    {
        return wim;
    }


    public Optional getInitData()
    {
        return Optional.ofNullable(initData);
    }


    public <T> Optional<T> getInitData(final Class<T> clazz)
    {
        return clazz.isInstance(initData) ? Optional.of((T)initData) : Optional.empty();
    }


    public void setInitData(final Object initData)
    {
        this.initData = initData;
    }
}

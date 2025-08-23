/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.impl.ComponentWidgetAdapter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.Div;

/**
 * Abstract class combining common functionality of cockpit components, e.g. {@link Editor} and {@link Action}.
 */
public abstract class AbstractCockpitComponent extends Div
{
    public static final String COCKPIT_COMPONENTS_ATTIBUTE = "_cockpitComponents";
    private static final long serialVersionUID = 8429789809995805037L;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCockpitComponent.class);
    protected transient WidgetInstanceManager widgetInstanceManager;
    protected transient ModelObserver modelObserver;
    protected boolean initialized;
    private transient CockpitComponentDefinitionService componentDefinitionService;


    public abstract String getComponentID();


    public abstract List<ComponentWidgetAdapterAware> getWidgetAdaptersAwareIfPresent();


    public void destroy()
    {
        unregisterObserver();
        unregisterWidgetStubInstance();
    }


    protected void unregisterObserver()
    {
        if(widgetInstanceManager != null && modelObserver != null)
        {
            widgetInstanceManager.getModel().removeObserver(modelObserver);
            modelObserver = null;
        }
    }


    public void unregisterWidgetStubInstance()
    {
        final List<ComponentWidgetAdapterAware> adapters = getWidgetAdaptersAwareIfPresent();
        for(final ComponentWidgetAdapterAware adapter : adapters)
        {
            if(adapter != null)
            {
                adapter.unregisterStubInstance();
                LOG.debug("Stub removed: {}", adapter);
            }
        }
    }


    public boolean isInitialized()
    {
        return initialized;
    }


    protected void initializeComponentWidgetAdapter(final ComponentWidgetAdapterAware componentWidgetAdapterAware)
    {
        componentWidgetAdapterAware.initialize(getComponentWidgetAdapter(), getComponentID());
    }


    public ComponentWidgetAdapter getComponentWidgetAdapter()
    {
        return (ComponentWidgetAdapter)SpringUtil.getBean("componentWidgetAdapter");
    }


    public CockpitComponentDefinitionService getComponentDefinitionService()
    {
        if(componentDefinitionService == null)
        {
            componentDefinitionService = (CockpitComponentDefinitionService)SpringUtil.getBean("componentDefinitionService");
        }
        return componentDefinitionService;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public void setComponentDefinitionService(final CockpitComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }
}

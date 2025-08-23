/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import java.util.Objects;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component responsible for rendering a group of cockpit actions.
 */
public class Actions extends AbstractCockpitElementsContainer
{
    private static final Logger LOG = LoggerFactory.getLogger(Actions.class);
    private transient Object inputValue;
    private String viewMode;
    private transient com.hybris.cockpitng.core.config.impl.jaxb.hybris.Actions configuration;


    @Override
    public void doInitialize()
    {
        configuration = null;
        try
        {
            configuration = getConfiguration(getConfig(), com.hybris.cockpitng.core.config.impl.jaxb.hybris.Actions.class);
        }
        catch(final CockpitConfigurationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("unable to load Actions configuration for '" + getConfig() + "'", e);
            }
        }
        this.getChildren().clear();
        if(configuration != null)
        {
            final CockpitComponentsRenderer renderer = (CockpitComponentsRenderer)createRenderer();
            renderer.render(this, configuration);
        }
    }


    public com.hybris.cockpitng.core.config.impl.jaxb.hybris.Actions getConfiguration()
    {
        return configuration;
    }


    public Object getInputValue()
    {
        return inputValue;
    }


    public void setInputValue(final Object value)
    {
        if((!ClassUtils.isPrimitiveOrWrapper(value == null ? null : value.getClass()))
                        || (!Objects.equals(this.inputValue, value)))
        {
            this.inputValue = value;
            reloadIfNecessary();
        }
    }


    public String getViewMode()
    {
        return viewMode;
    }


    public void setViewMode(final String viewMode)
    {
        if(!Objects.equals(this.viewMode, viewMode))
        {
            this.viewMode = viewMode;
            reloadIfNecessary();
        }
    }


    @Override
    public Object createDefaultRenderer()
    {
        return new DefaultCockpitActionsRenderer();
    }
}

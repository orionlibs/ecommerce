/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component responsible for rendering a group of editors.
 */
public class Editors extends AbstractCockpitElementsContainer
{
    private static final long serialVersionUID = -3843194080273375854L;
    private static final Logger LOG = LoggerFactory.getLogger(Editors.class);


    @Override
    public void doInitialize()
    {
        com.hybris.cockpitng.core.config.impl.jaxb.hybris.Editors configuration = null;
        try
        {
            configuration = getConfiguration(getConfig(), com.hybris.cockpitng.core.config.impl.jaxb.hybris.Editors.class);
        }
        catch(final CockpitConfigurationNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Couldn't find editors configuration for '" + getConfig() + "'", e);
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn("Unable to load editors configuration for '" + getConfig() + "'", e);
        }
        if(configuration == null)
        {
            this.getChildren().clear();
        }
        else
        {
            final CockpitComponentsRenderer renderer = (CockpitComponentsRenderer)createRenderer();
            renderer.render(this, configuration);
        }
    }


    @Override
    public Object createDefaultRenderer()
    {
        return new DefaultCockpitEditorsRenderer();
    }
}

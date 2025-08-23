/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.springframework.beans.factory.annotation.Required;

public class DefaultElementConfigurationProvider implements ElementConfigurationProvider
{
    private static final String SETTING_CONFIGURATION_CONTEXT = "configCtx";
    private static final String DEFAULT_CONFIGURATION_CONTEXT = "compare-view";
    private CockpitConfigurationService cockpitConfigurationService;


    @Override
    public CompareView getConfiguration(final WidgetInstanceManager widgetInstanceManager, final String elementTypeCode)
                    throws CockpitConfigurationException
    {
        return getCockpitConfigurationService().loadConfiguration(
                        new DefaultConfigContext(widgetInstanceManager.getWidgetSettings()
                                        .getOrDefault(SETTING_CONFIGURATION_CONTEXT, DEFAULT_CONFIGURATION_CONTEXT).toString(), elementTypeCode),
                        CompareView.class, widgetInstanceManager.getWidgetslot().getWidgetInstance());
    }


    @Override
    public CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public interface ElementConfigurationProvider
{
    CompareView getConfiguration(final WidgetInstanceManager widgetInstanceManager, final String elementTypeCode)
                    throws CockpitConfigurationException;


    CockpitConfigurationService getCockpitConfigurationService();
}

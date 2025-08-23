/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.util.template.TemplateValuesProvider;
import java.util.Collections;
import java.util.Map;
import org.zkoss.zk.ui.Component;

/**
 * Provides widget settings and assigns to {@link #VALUE_NAME_SETTINGS} value.
 */
public class WidgetSettingsTemplateValuesProvider implements TemplateValuesProvider
{
    public static final String VALUE_NAME_SETTINGS = "settings";


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        if(context instanceof Component)
        {
            Component cmp = (Component)context;
            while(!(cmp instanceof Widgetslot) && cmp != null)
            {
                cmp = cmp.getParent();
            }
            if(cmp != null)
            {
                final Widgetslot slot = (Widgetslot)cmp;
                return Collections.singletonMap(VALUE_NAME_SETTINGS, slot.getSettings());
            }
            else
            {
                return Collections.emptyMap();
            }
        }
        else
        {
            return Collections.emptyMap();
        }
    }
}

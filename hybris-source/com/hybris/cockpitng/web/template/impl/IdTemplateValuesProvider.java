/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import com.hybris.cockpitng.core.util.template.TemplateValuesProvider;
import java.util.Collections;
import java.util.Map;
import org.zkoss.zk.ui.Component;

/**
 * Provides container id available in {@link org.zkoss.zk.ui.Component} and assigns to {@link #VALUE_NAME_ID} value.
 */
public class IdTemplateValuesProvider implements TemplateValuesProvider
{
    public static final String VALUE_NAME_ID = "id";


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        if(context instanceof Component)
        {
            return Collections.singletonMap(VALUE_NAME_ID, ((Component)context).getUuid());
        }
        else
        {
            return Collections.emptyMap();
        }
    }
}

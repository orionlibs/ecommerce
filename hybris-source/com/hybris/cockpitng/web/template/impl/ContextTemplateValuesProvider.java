/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import com.hybris.cockpitng.web.template.TemplateValuesProvider;
import java.util.Collections;
import java.util.Map;
import org.zkoss.zk.ui.Component;

/**
 * Provides template context and assigns to {@link #VALUE_NAME_CONTEXT} value.
 *
 * @see com.hybris.cockpitng.core.util.template.impl.ContextTemplateValuesProvider
 * @deprecated since 6.5
 */
@Deprecated(since = "6.5", forRemoval = true)
public class ContextTemplateValuesProvider implements TemplateValuesProvider
{
    public static final String VALUE_NAME_CONTEXT = "context";


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        if(context instanceof Component)
        {
            return Collections.singletonMap(VALUE_NAME_CONTEXT, context);
        }
        else
        {
            return Collections.emptyMap();
        }
    }
}

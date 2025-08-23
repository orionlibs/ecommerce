/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.template.impl;

import com.hybris.cockpitng.core.util.template.TemplateValuesProvider;
import java.util.Collections;
import java.util.Map;

/**
 * Provides template context and assigns to {@link #VALUE_NAME_CONTEXT} value.
 */
public class ContextTemplateValuesProvider implements TemplateValuesProvider
{
    private static final String VALUE_NAME_CONTEXT = "context";


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        return Collections.singletonMap(VALUE_NAME_CONTEXT, context);
    }
}

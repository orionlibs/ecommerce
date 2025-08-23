/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.web.template.TemplateValuesProvider;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provides all properties available in {@link CockpitProperties} and assigns to {@link #VALUE_NAME_PROPERTIES} value.
 *
 * @see com.hybris.cockpitng.core.util.template.impl.PropertiesTemplateValuesProvider
 * @deprecated since 6.5
 */
@Deprecated(since = "6.5", forRemoval = true)
public class PropertiesTemplateValuesProvider implements TemplateValuesProvider
{
    public static final String VALUE_NAME_PROPERTIES = "properties";
    private CockpitProperties cockpitProperties;


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        return Collections.singletonMap(VALUE_NAME_PROPERTIES, getCockpitProperties().getProperties());
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}

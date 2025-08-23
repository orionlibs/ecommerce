/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import com.hybris.cockpitng.core.util.template.TemplateValuesProvider;
import com.hybris.cockpitng.util.js.JsWidgetSessionDTO;
import com.hybris.cockpitng.util.js.JsWidgetSessionInfoCollector;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provides template context and assigns to {@link #VALUE_NAME_CONTEXT} value.
 */
public class SessionInfoTemplateValuesProvider implements TemplateValuesProvider
{
    public static final String VALUE_NAME_CONTEXT = "sessionInfo";
    private JsWidgetSessionInfoCollector collector;


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        final JsWidgetSessionDTO dto = getCollector().gatherSessionInfo();
        return Collections.singletonMap(VALUE_NAME_CONTEXT, dto);
    }


    protected JsWidgetSessionInfoCollector getCollector()
    {
        return collector;
    }


    @Required
    public void setCollector(final JsWidgetSessionInfoCollector collector)
    {
        this.collector = collector;
    }
}

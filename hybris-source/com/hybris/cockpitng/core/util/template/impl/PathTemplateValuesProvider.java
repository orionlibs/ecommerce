/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.template.impl;

import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.impl.WidgetRequest;
import com.hybris.cockpitng.core.util.impl.WidgetRequestUtils;
import com.hybris.cockpitng.core.util.template.TemplateValuesProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provider that uses template identity as a full resource path and assigns it as follows:
 * <ol>
 * 	<li>all before widget's id is assigned to {@link #VALUE_NAME_ROOT}</li>
 * 	<li>widget's id is assigned to {@link #VALUE_NAME_WIDGET}</li>
 * 	<li>path after widget's id is assigned to {@link #VALUE_NAME_VIEW_PATH}</li>
 * 	<li>full URL (incl. potential parameters) after widget's id is assigned to {@link #VALUE_NAME_VIEW_URL}</li>
 * </ol>
 */
public class PathTemplateValuesProvider implements TemplateValuesProvider
{
    private static final String VALUE_NAME_ROOT = "root";
    private static final String VALUE_NAME_WIDGET = "widget";
    private static final String VALUE_NAME_VIEW_PATH = "viewPath";
    private static final String VALUE_NAME_VIEW_URL = "viewURL";
    private WidgetLibUtils widgetLibUtils;


    @Override
    public Map<String, Object> provideTemplateValues(final Object context, final String templateId)
    {
        final String requestURI = WidgetRequestUtils.cleanupRequestUri(templateId);
        final WidgetRequest parsedRequest = WidgetRequestUtils.parseRequestURI(getWidgetLibUtils(), requestURI);
        if(parsedRequest != null)
        {
            final Map<String, Object> result = new HashMap<>();
            result.put(VALUE_NAME_ROOT, parsedRequest.getRoot() != null ? "/" + parsedRequest.getRoot() : "");
            result.put(VALUE_NAME_WIDGET, parsedRequest.getWidgetId());
            result.put(VALUE_NAME_VIEW_PATH, parsedRequest.getResourcePath());
            result.put(VALUE_NAME_VIEW_URL, parsedRequest.getResource().toString());
            return result;
        }
        return Collections.emptyMap();
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }
}

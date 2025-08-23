/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.template.impl;

import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.util.impl.WidgetRequest;
import com.hybris.cockpitng.core.util.impl.WidgetRequestUtils;
import com.hybris.cockpitng.web.template.TemplateResolver;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Required;

/**
 * Resolver that uses template identity as a full resource path and asks for such resource {@link CockpitResourceLoader}
 * . If a resource if found then its contents are returned as template.
 *
 * @see com.hybris.cockpitng.core.util.template.impl.PathTemplateResolver
 * @deprecated since 6.5
 */
@Deprecated(since = "6.5", forRemoval = true)
public class PathTemplateResolver implements TemplateResolver
{
    private WidgetLibUtils widgetLibUtils;
    private CockpitResourceLoader cockpitResourceLoader;


    @Override
    public InputStream resolveTemplate(final String templateId) throws IOException
    {
        final WidgetRequest request = WidgetRequestUtils.parseRequestURI(getWidgetLibUtils(), templateId);
        final String widgetID = request != null ? request.getWidgetId() : null;
        final String resourceName = request != null ? request.getResourcePath() : null;
        WidgetJarLibInfo widgetJarLibInfo = null;
        if(widgetID != null)
        {
            widgetJarLibInfo = getWidgetLibUtils().getWidgetJarLibInfo(widgetID);
        }
        if(widgetJarLibInfo != null)
        {
            return getCockpitResourceLoader().getResourceAsStream(widgetJarLibInfo, resourceName);
        }
        else
        {
            return getCockpitResourceLoader().getResourceAsStream(templateId);
        }
    }


    protected CockpitResourceLoader getCockpitResourceLoader()
    {
        return cockpitResourceLoader;
    }


    @Required
    public void setCockpitResourceLoader(final CockpitResourceLoader cockpitResourceLoader)
    {
        this.cockpitResourceLoader = cockpitResourceLoader;
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

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.template.impl;

import com.hybris.cockpitng.core.util.template.TemplateEngine;
import com.hybris.cockpitng.core.util.template.TemplateResolver;
import com.hybris.cockpitng.core.util.template.TemplateValuesProvider;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Basic template engine implementation
 */
public abstract class AbstractTemplateEngine implements TemplateEngine
{
    private TemplateResolver templateResolver;
    private List<TemplateValuesProvider> valuesProviders = new ArrayList<>();


    @Override
    public InputStream applyTemplate(final Object context, final String templateId, final Map<String, Object> values)
                    throws IOException
    {
        final Map<String, Object> allValues = new HashMap<>(values);
        valuesProviders.forEach(provider -> allValues.putAll(provider.provideTemplateValues(context, templateId)));
        try(final InputStream template = templateResolver.resolveTemplate(templateId))
        {
            if(template != null)
            {
                return applyTemplate(context, templateId, template, allValues);
            }
            else
            {
                throw new IOException("Unable to resolve template for id: " + templateId);
            }
        }
    }


    protected abstract InputStream applyTemplate(final Object context, final String templateId, final InputStream template,
                    final Map<String, Object> values) throws IOException;


    public TemplateResolver getTemplateResolver()
    {
        return templateResolver;
    }


    @Required
    public void setTemplateResolver(final TemplateResolver templateResolver)
    {
        this.templateResolver = templateResolver;
    }


    public List<TemplateValuesProvider> getValuesProviders()
    {
        return valuesProviders;
    }


    public void setValuesProviders(final List<TemplateValuesProvider> valuesProviders)
    {
        this.valuesProviders = valuesProviders;
    }
}

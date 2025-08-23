/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n.impl;

import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.i18n.LocalizedValuesService;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of service for reading localized values {@link LocalizedValuesService}
 */
public class DefaultLocalizedValuesService implements LocalizedValuesService
{
    private CockpitLocaleService cockpitLocaleService;


    @Override
    public String getLanguageLabelKey(final Locale locale)
    {
        return locale.toString();
    }


    @Override
    public Locale getSelectedLocaleOrDefault(final Map<Locale, Object> values)
    {
        if(values != null && values.size() == 1)
        {
            return values.entrySet().iterator().next().getKey();
        }
        return getCockpitLocaleService().getCurrentLocale();
    }


    @Override
    public Object getCurrentValue(final Locale currentLocale, final Map<Locale, Object> values)
    {
        if(values != null && values.containsKey(currentLocale))
        {
            return values.get(currentLocale);
        }
        return null;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }
}

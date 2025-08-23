/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelProvider;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class LocaleLabelProvider implements LabelProvider<Locale>
{
    private CockpitLocaleService cockpitLocaleService;


    @Override
    public String getLabel(final Locale locale)
    {
        String label = null;
        if(locale != null)
        {
            label = locale.toString();
        }
        return label;
    }


    @Override
    public String getDescription(final Locale locale)
    {
        return locale.getDisplayName(getCockpitLocaleService().getCurrentLocale());
    }


    @Override
    public String getIconPath(final Locale locale)
    {
        return null;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }
}

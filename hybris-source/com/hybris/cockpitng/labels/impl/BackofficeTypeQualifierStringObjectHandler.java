/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.i18n.CockpitLocaleService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

/**
 * Extended version of {@link TypeQualifierStringObjectHandler} that uses {@link CockpitLocaleService} to get labels in
 * the current cockpit session locale.
 */
public class BackofficeTypeQualifierStringObjectHandler extends TypeQualifierStringObjectHandler
{
    private CockpitLocaleService cockpitLocaleService;


    @Override
    public Locale getCurrentLocale()
    {
        return cockpitLocaleService.getCurrentLocale();
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }
}

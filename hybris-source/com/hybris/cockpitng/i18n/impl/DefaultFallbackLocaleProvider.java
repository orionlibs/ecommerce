/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n.impl;

import com.hybris.cockpitng.i18n.FallbackLocaleProvider;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFallbackLocaleProvider implements FallbackLocaleProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFallbackLocaleProvider.class);


    @Override
    public List<Locale> getFallbackLocales(final Locale locale)
    {
        LOG.warn("Could not get fallback locale. Please write your own implementation of FallbackLocaleProvider");
        return Collections.emptyList();
    }
}

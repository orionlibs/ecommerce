/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.locales.factory.impl;

import com.hybris.cockpitng.config.locales.factory.CockpitLocalesFactory;
import com.hybris.cockpitng.config.locales.jaxb.CockpitLocale;
import com.hybris.cockpitng.config.locales.jaxb.CockpitLocales;
import com.hybris.cockpitng.config.locales.jaxb.ObjectFactory;
import java.util.Collection;

public class DefaultCockpitLocalesFactory implements CockpitLocalesFactory
{
    private final ObjectFactory jaxbFactory;


    public DefaultCockpitLocalesFactory(final ObjectFactory jaxbFactory)
    {
        this.jaxbFactory = jaxbFactory;
    }


    @Override
    public CockpitLocales createCockpitLocales()
    {
        return jaxbFactory.createCockpitLocales();
    }


    @Override
    public CockpitLocales createCockpitLocales(final Collection<CockpitLocale> locales)
    {
        final CockpitLocales cockpitLocales = jaxbFactory.createCockpitLocales();
        cockpitLocales.getCockpitLocale().addAll(locales);
        return cockpitLocales;
    }
}

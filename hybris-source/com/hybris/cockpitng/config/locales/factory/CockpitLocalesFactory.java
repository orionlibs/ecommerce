/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.locales.factory;

import com.hybris.cockpitng.config.locales.jaxb.CockpitLocale;
import com.hybris.cockpitng.config.locales.jaxb.CockpitLocales;
import java.util.Collection;

public interface CockpitLocalesFactory
{
    CockpitLocales createCockpitLocales();


    CockpitLocales createCockpitLocales(final Collection<CockpitLocale> locales);
}

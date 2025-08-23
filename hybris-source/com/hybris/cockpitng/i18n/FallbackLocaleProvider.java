/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n;

import java.util.List;
import java.util.Locale;

public interface FallbackLocaleProvider
{
    List<Locale> getFallbackLocales(Locale locale);
}

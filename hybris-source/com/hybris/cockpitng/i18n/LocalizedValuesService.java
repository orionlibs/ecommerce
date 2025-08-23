/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.i18n;

import java.util.Locale;
import java.util.Map;

/**
 * Service for reading localized values
 */
public interface LocalizedValuesService
{
    /**
     * Builds language label key based on given locale
     *
     * @param locale an localized object to use
     * @return key of the label
     */
    String getLanguageLabelKey(final Locale locale);


    /**
     * Retrieves the first locale from the map if only one element is present
     * Otherwise it returns default locale
     *
     * @param values from which to retrieve locale
     * @return the first locale from the map or default locale
     */
    Locale getSelectedLocaleOrDefault(final Map<Locale, Object> values);


    /**
     * Returns value from given map for given key
     *
     * @param currentLocale key based on which value is retrieved
     * @param values map from which value is retrieved
     * @return value from given map for given key
     */
    Object getCurrentValue(final Locale currentLocale, final Map<Locale, Object> values);
}

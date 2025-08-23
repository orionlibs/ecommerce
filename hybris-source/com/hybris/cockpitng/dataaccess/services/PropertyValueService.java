/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Reads property values from an object.
 */
public interface PropertyValueService
{
    /**
     * Read all property values from source object.
     *
     * @param propertyQualifiers
     *           a list of property qualifiers to read
     * @param sourceObject
     *           object to read from
     * @return map contains all property qualifiers with corresponding values.
     */
    Map<String, Object> readValues(Object sourceObject, List<String> propertyQualifiers);


    /**
     * Read property values from source object. Every property is first check, whether user may read it.
     *
     * @param propertyQualifiers
     *           a list of property qualifiers to read
     * @param sourceObject
     *           object to read from
     * @return map contains all property qualifiers with corresponding read result.
     */
    default Map<String, PropertyReadResult> readRestrictedValues(final Object sourceObject, final List<String> propertyQualifiers)
    {
        final Map<String, PropertyReadResult> result = new HashMap<>();
        for(final Map.Entry<String, Object> value : readValues(sourceObject, propertyQualifiers).entrySet())
        {
            result.put(value.getKey(), new PropertyReadResult(value.getValue()));
        }
        return result;
    }


    /**
     * Read property value from source object.
     *
     * @param propertyQualifiers
     *           property qualifier to read
     * @param sourceObject
     *           object to read from
     * @return corresponding property value.
     */
    Object readValue(Object sourceObject, String propertyQualifiers);


    /**
     * Read property value from source object. Property is first check, whether user may read it.
     *
     * @param propertyQualifiers
     *           property qualifier to read
     * @param sourceObject
     *           object to read from
     * @return corresponding property read result.
     */
    default PropertyReadResult readRestrictedValue(final Object sourceObject, final String propertyQualifiers)
    {
        return new PropertyReadResult(readValue(sourceObject, propertyQualifiers));
    }


    /**
     * Read localized qualifier values from source object. The returning map holds a locale as key and the value of this map
     * holds the corresponding localized value of the property.
     *
     * @param sourceObject
     *           object to read from
     * @param propertyQualifier
     *           property qualifier to read
     * @param locales
     *           a list of locales to read
     * @return map where the key holds a @Locale object and the value of this map holds the corresponding value of the
     *         qualifier.
     */
    Map<Locale, Object> readValue(Object sourceObject, String propertyQualifier, List<Locale> locales);


    /**
     * Read localized qualifier values from source object. Every property is first check, whether user may read it. The
     * returning map holds a locale as key and the value of this map holds the corresponding localized value of the
     * property.
     *
     * @param sourceObject
     *           object to read from
     * @param propertyQualifier
     *           property qualifier to read
     * @param locales
     *           a list of locales to read
     * @return map where the key holds a @Locale object and the value of this map holds the corresponding value of the
     *         qualifier.
     */
    default Map<Locale, PropertyReadResult> readRestrictedValue(final Object sourceObject, final String propertyQualifier,
                    final List<Locale> locales)
    {
        final Map<Locale, PropertyReadResult> result = new HashMap<>();
        for(final Map.Entry<Locale, Object> value : readValue(sourceObject, propertyQualifier, locales).entrySet())
        {
            result.put(value.getKey(), new PropertyReadResult(value.getValue()));
        }
        return result;
    }


    /**
     * Read qualifiers values from the source object. Also Localized property values will be read.
     *
     * @param sourceObject
     *           is a instance of a type.
     * @param propertyQualifiers
     *           is a list of the property qualifiers of the type.
     * @param locales
     *           is a list of locales.
     * @return map contains all qualifiers with corresponding localized values.
     */
    Map<String, Object> readValues(Object sourceObject, List<String> propertyQualifiers, List<Locale> locales);


    /**
     * Read qualifiers values from the source object. Also Localized property values will be read. Every property is first
     * check, whether user may read it.
     *
     * @param sourceObject
     *           is a instance of a type.
     * @param propertyQualifiers
     *           is a list of the property qualifiers of the type.
     * @param locales
     *           is a list of locales.
     * @return map contains all qualifiers with corresponding localized values.
     */
    default Map<String, PropertyReadResult> readRestrictedValues(final Object sourceObject, final List<String> propertyQualifiers,
                    final List<Locale> locales)
    {
        final Map<String, PropertyReadResult> result = new HashMap<>();
        for(final Map.Entry<String, Object> value : readValues(sourceObject, propertyQualifiers, locales).entrySet())
        {
            result.put(value.getKey(), new PropertyReadResult(value.getValue()));
        }
        return result;
    }


    /**
     * Sets the value for the specified property and locale. Assumes the the object has a setter with a {@link Locale} as
     * second argument for the given property.
     *
     * @param sourceObject
     *           a object to write a property with given qualifier and locale
     * @param propertyQualifier
     *           qualifier of property value to set
     * @param value
     *           value to set
     * @param locale
     *           locale for property value
     */
    void setLocalizedValue(Object sourceObject, String propertyQualifier, Object value, Locale locale);
}

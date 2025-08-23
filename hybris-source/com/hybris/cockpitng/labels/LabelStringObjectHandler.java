/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels;

import java.util.Locale;

/**
 * Special handler for getting label representations for {@link String} object. Can be used by {@link LabelService} to
 * override the default behavior of returning the string itself for a string object.
 */
public interface LabelStringObjectHandler
{
    /**
     * @return currently used locale
     */
    default Locale getCurrentLocale()
    {
        return Locale.getDefault();
    }


    /**
     * @return label for an object described by the given key.
     */
    String getObjectLabel(String key);


    /**
     * @return text description for an object described by the given key.
     */
    String getObjectDescription(String key);


    /**
     * @return icon path for an object described by the given key.
     */
    String getObjectIconPath(String key);
}

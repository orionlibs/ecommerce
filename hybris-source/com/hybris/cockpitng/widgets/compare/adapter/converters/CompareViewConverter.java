/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter.converters;

public interface CompareViewConverter<SOURCE, TARGET>
{
    /**
     * Converts source object to target type object
     *
     * @param source
     *           object to be converted
     * @return converted object
     */
    TARGET convert(final SOURCE source);
}

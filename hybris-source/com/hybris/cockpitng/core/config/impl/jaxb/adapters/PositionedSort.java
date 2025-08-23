/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.adapters;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.util.List;

/**
 * Provides service to sort list of {@code Positioned}
 */
public interface PositionedSort<T extends Positioned>
{
    /**
     * Sorts list of {@code Positioned}
     *
     * @param list {@code Positioned}
     */
    void sort(List<T> list);
}

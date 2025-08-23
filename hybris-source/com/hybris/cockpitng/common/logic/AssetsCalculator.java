/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.logic;

import java.util.List;

public interface AssetsCalculator
{
    /**
     * Calculates assets of a given object
     *
     * @param assetsGroups
     *           list of asset groups, for which number of assets will be calculated
     * @param object
     *           for which number of assets should be calculated
     * @return number of assets for a given object represented by an integer value
     */
    int calculateAssets(final List<String> assetsGroups, final Object object);
}

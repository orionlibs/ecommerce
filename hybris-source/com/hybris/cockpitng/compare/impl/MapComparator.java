/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.impl;

import com.hybris.cockpitng.compare.ObjectAttributeComparator;
import java.util.Map;

public class MapComparator implements ObjectAttributeComparator<Map>
{
    @Override
    public boolean isEqual(final Map map1, final Map map2)
    {
        return map1.size() == map2.size() && map1.entrySet().containsAll(map2.entrySet());
    }
}

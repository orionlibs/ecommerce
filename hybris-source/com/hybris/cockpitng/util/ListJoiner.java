/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListJoiner
{
    /**
     * Adds all elements from all not-null collections.
     *
     * @param collections
     * @return list which contains elements from all not-null collections
     */
    public static final List join(final Collection... collections)
    {
        return (List)Arrays.stream(collections).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }
}

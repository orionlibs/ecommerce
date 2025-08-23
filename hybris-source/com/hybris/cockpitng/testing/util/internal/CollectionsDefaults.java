/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyIterator;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyListIterator;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

final class CollectionsDefaults
{
    private static final Map<Class<?>, Object> DEFAULTS = new HashMap<>();

    static
    {
        DEFAULTS.put(List.class, emptyList());
        DEFAULTS.put(Set.class, emptySet());
        DEFAULTS.put(Map.class, emptyMap());
        DEFAULTS.put(ListIterator.class, emptyListIterator());
        DEFAULTS.put(Iterator.class, emptyIterator());
    }

    private CollectionsDefaults()
    {
        throw new AssertionError();
    }


    @Nullable
    public static <T> T defaultValue(final Class<T> type)
    {
        return (T)DEFAULTS.get(checkNotNull(type));
    }


    public static <T> boolean hasDefaultValue(final Class<T> type)
    {
        return DEFAULTS.containsKey(type);
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DefaultContextSearchRestriction implements ContextSearchRestriction
{
    private final Set<String> restrictions;


    public DefaultContextSearchRestriction(final String... attributes)
    {
        this(Arrays.asList(attributes));
    }


    public DefaultContextSearchRestriction(final List<String> attributes)
    {
        this.restrictions = new LinkedHashSet<>(attributes);
    }


    @Override
    public boolean isAttributeRestricted(final String name)
    {
        return restrictions.contains(name);
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final DefaultContextSearchRestriction that = (DefaultContextSearchRestriction)o;
        return restrictions.equals(that.restrictions);
    }


    @Override
    public int hashCode()
    {
        return restrictions.hashCode();
    }
}

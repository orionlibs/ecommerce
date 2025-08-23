/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Simple pojo representing localized qualifier. It contains the qualifier name and the collection of locales.
 */
public class LocalizedQualifier
{
    private final String name;
    private final Collection<Locale> locales;


    public LocalizedQualifier(final String name, final Collection<Locale> locales)
    {
        this.name = name;
        this.locales = ImmutableList.copyOf(locales);
    }


    public LocalizedQualifier(final String name)
    {
        this.name = name;
        this.locales = Collections.emptyList();
    }


    public String getName()
    {
        return name;
    }


    public Collection<Locale> getLocales()
    {
        return locales;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final LocalizedQualifier that = (LocalizedQualifier)o;
        return Objects.equals(name, that.name) && Objects.equals(locales, that.locales);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(name, locales);
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("name", name).append("locales", locales).toString();
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.model;

import java.util.Locale;

public class CompareLocalizedAttributeDescriptor extends CompareAttributeDescriptor
{
    private Locale locale;


    public CompareLocalizedAttributeDescriptor(final String qualifier, final Locale locale)
    {
        super(qualifier);
        this.locale = locale;
    }


    public CompareLocalizedAttributeDescriptor(final String qualifier, final String group, final Locale locale)
    {
        super(qualifier, group);
        this.locale = locale;
    }


    public Locale getLocale()
    {
        return locale;
    }


    protected void setLocale(final Locale locale)
    {
        this.locale = locale;
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
        if(!super.equals(o))
        {
            return false;
        }
        final CompareLocalizedAttributeDescriptor that = (CompareLocalizedAttributeDescriptor)o;
        return locale.equals(that.locale);
    }


    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + locale.hashCode();
        return result;
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.web.js.impl;

/**
 * Meta-data of dependency
 */
public class DependencyDefinition
{
    private final String name;
    private final String version;


    public DependencyDefinition(final String name, final String version)
    {
        this.name = name;
        this.version = version;
    }


    public String getName()
    {
        return name;
    }


    public String getVersion()
    {
        return version;
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
        final DependencyDefinition that = (DependencyDefinition)o;
        if(name != null ? !name.equals(that.name) : that.name != null)
        {
            return false;
        }
        return !(version != null ? !version.equals(that.version) : that.version != null);
    }


    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}

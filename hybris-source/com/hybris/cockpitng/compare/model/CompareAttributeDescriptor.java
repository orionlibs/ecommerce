/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.model;

/**
 * Class holds information about comparing object's attributes. The same attribute may be placed in two different group
 * so group name is also required field.
 */
public class CompareAttributeDescriptor
{
    /**
     * Attribute's qualifier
     */
    private String qualifier;
    /**
     * Group name
     */
    private String group;
    /**
     * If attribute is readonly
     */
    private Boolean readonly;


    public CompareAttributeDescriptor(final String qualifier)
    {
        this.qualifier = qualifier;
    }


    public CompareAttributeDescriptor(final String qualifier, final String group)
    {
        this(qualifier);
        this.group = group;
    }


    public String getQualifier()
    {
        return qualifier;
    }


    public void setQualifier(final String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getGroup()
    {
        return group;
    }


    public void setGroup(final String group)
    {
        this.group = group;
    }


    public Boolean isReadonly()
    {
        if(this.readonly == null)
        {
            return false;
        }
        else
        {
            return readonly;
        }
    }


    public void setReadonly(final boolean readonly)
    {
        this.readonly = readonly;
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
        final CompareAttributeDescriptor that = (CompareAttributeDescriptor)o;
        if(!qualifier.equals(that.qualifier))
        {
            return false;
        }
        if(((CompareAttributeDescriptor)o).isReadonly().equals(Boolean.TRUE) && readonly == null)
        {
            return false;
        }
        if(readonly != null && !((CompareAttributeDescriptor)o).isReadonly().equals(readonly))
        {
            return false;
        }
        if(((CompareAttributeDescriptor)o).getGroup() == null && group == null)
        {
            return true;
        }
        else if(((CompareAttributeDescriptor)o).getGroup() == null || group == null)
        {
            return false;
        }
        return group.equals(that.group);
    }


    @Override
    public int hashCode()
    {
        int result = qualifier.hashCode();
        if(group != null)
        {
            result = 31 * result + group.hashCode();
        }
        return result;
    }


    @Override
    public String toString()
    {
        return qualifier + (group != null ? '@' + group : "");
    }
}

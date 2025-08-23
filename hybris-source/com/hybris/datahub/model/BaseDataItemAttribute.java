package com.hybris.datahub.model;

import javax.annotation.concurrent.Immutable;

@Immutable
public class BaseDataItemAttribute implements DataItemAttribute
{
    private final boolean isLocalizable;
    private final boolean isSecured;
    private final boolean isCollection;
    private final Class<?> clazz;
    private final String attributeName;
    private final String itemType;


    public BaseDataItemAttribute(boolean isLocalizable, Class<?> clazz, String attributeName, boolean isSecured, String itemType, boolean isCollection)
    {
        this.isLocalizable = isLocalizable;
        this.clazz = clazz;
        this.attributeName = attributeName;
        this.isSecured = isSecured;
        this.itemType = itemType;
        this.isCollection = isCollection;
    }


    public String getPropertyName()
    {
        return this.attributeName;
    }


    public Class<?> getPropertyType()
    {
        return this.clazz;
    }


    public boolean isLocalizable()
    {
        return this.isLocalizable;
    }


    public boolean isCollection()
    {
        return this.isCollection;
    }


    public boolean isSecured()
    {
        return this.isSecured;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        BaseDataItemAttribute that = (BaseDataItemAttribute)o;
        if(this.isCollection != that.isCollection)
        {
            return false;
        }
        if(this.isLocalizable != that.isLocalizable)
        {
            return false;
        }
        if(this.isSecured != that.isSecured)
        {
            return false;
        }
        if(!this.itemType.equals(that.itemType))
        {
            return false;
        }
        if(!this.attributeName.equals(that.attributeName))
        {
            return false;
        }
        if(!this.clazz.equals(that.clazz))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = this.attributeName.hashCode();
        result = 31 * result + this.itemType.hashCode();
        result = 31 * result + (this.isSecured ? 1 : 0);
        result = 31 * result + (this.isCollection ? 1 : 0);
        result = 31 * result + (this.isLocalizable ? 1 : 0);
        result = 31 * result + this.clazz.hashCode();
        return result;
    }


    public String toString()
    {
        return "BaseDataItemAttribute{isLocalizable=" + this.isLocalizable + ", isSecured=" + this.isSecured + ", isCollection=" + this.isCollection + ", clazz=" + this.clazz + ", attributeName='" + this.attributeName + "', itemType='" + this.itemType + "'}";
    }
}

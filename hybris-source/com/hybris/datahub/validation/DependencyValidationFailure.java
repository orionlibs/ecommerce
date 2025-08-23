package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.ItemTypeData;

public abstract class DependencyValidationFailure extends ValidationFailure
{
    protected final ItemTypeData typeData;
    protected final String dependency;


    public DependencyValidationFailure(ItemTypeData type, String dep, String msg)
    {
        super(msg, ValidationFailureType.FATAL);
        this.dependency = dep;
        this.typeData = type;
    }


    public ItemTypeData getSource()
    {
        return this.typeData;
    }


    public String getDependency()
    {
        return this.dependency;
    }


    public boolean equals(Object o)
    {
        if(o != null && getClass().isInstance(o))
        {
            DependencyValidationFailure that = (DependencyValidationFailure)o;
            return (this.dependency.equals(that.dependency) && this.typeData.equals(that.typeData));
        }
        return false;
    }


    public int hashCode()
    {
        return 31 * this.typeData.hashCode() + this.dependency.hashCode();
    }
}

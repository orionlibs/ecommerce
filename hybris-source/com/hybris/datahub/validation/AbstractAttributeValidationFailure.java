package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.AttributeData;

public abstract class AbstractAttributeValidationFailure extends ValidationFailure
{
    private static final String DEFAULT_PROPERTY_NAME = "name";
    private final transient AttributeData attributeData;


    public AbstractAttributeValidationFailure(AttributeData data, String msg, ValidationFailureType type)
    {
        this(data, "name", msg, type);
    }


    public AbstractAttributeValidationFailure(AttributeData data, String prop, String msg, ValidationFailureType type)
    {
        super(prop, msg, type);
        this.attributeData = data;
    }


    public AttributeData getAttributeData()
    {
        return this.attributeData;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o != null && getClass() == o.getClass())
        {
            assert this.attributeData != null : "Validation should have been failed before reaching this point";
            AbstractAttributeValidationFailure that = (AbstractAttributeValidationFailure)o;
            return this.attributeData.equals(that.attributeData);
        }
        return false;
    }


    public int hashCode()
    {
        int result = getClass().hashCode();
        result = 31 * result + this.attributeData.hashCode();
        return result;
    }
}

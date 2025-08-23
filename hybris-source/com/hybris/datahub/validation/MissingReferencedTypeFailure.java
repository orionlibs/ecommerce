package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.ItemTypeData;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class MissingReferencedTypeFailure extends ValidationFailure
{
    private final ItemTypeData typeDefinition;
    private final String invalidPropertyName;
    private final String referredType;


    public MissingReferencedTypeFailure(ItemTypeData src, String prop, String type)
    {
        super(prop, prop + " property in " + prop + " refers missing type " + src, ValidationFailureType.FATAL);
        this.typeDefinition = src;
        this.invalidPropertyName = prop;
        this.referredType = type;
    }


    public ItemTypeData getTypeDefinition()
    {
        return this.typeDefinition;
    }


    public String getInvalidPropertyName()
    {
        return this.invalidPropertyName;
    }


    public String getReferredType()
    {
        return this.referredType;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o instanceof MissingReferencedTypeFailure)
        {
            MissingReferencedTypeFailure that = (MissingReferencedTypeFailure)o;
            return (new EqualsBuilder())
                            .append(this.typeDefinition, that.typeDefinition)
                            .append(this.invalidPropertyName, that.invalidPropertyName)
                            .append(this.referredType, that.referredType)
                            .build().booleanValue();
        }
        return false;
    }


    public int hashCode()
    {
        return (new HashCodeBuilder())
                        .append(this.typeDefinition)
                        .append(this.invalidPropertyName)
                        .append(this.referredType)
                        .build().intValue();
    }
}

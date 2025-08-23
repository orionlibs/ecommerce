package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.AttributeData;

public class AttributeDisableOverrideFailure extends AbstractAttributeValidationFailure
{
    public AttributeDisableOverrideFailure(AttributeData data)
    {
        super(data, "disabled", message(data.getItemType(), data.getName()), ValidationFailureType.FATAL);
    }


    private static String message(String type, String name)
    {
        return String.format("Attribute %s in type %s contains both 'override' and 'disable' attributes", new Object[] {name, type});
    }
}

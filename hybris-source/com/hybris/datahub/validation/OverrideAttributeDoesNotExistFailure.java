package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.AttributeData;

public class OverrideAttributeDoesNotExistFailure extends AbstractAttributeValidationFailure
{
    public OverrideAttributeDoesNotExistFailure(AttributeData data)
    {
        super(data, message(data.getItemType(), data.getName()), ValidationFailureType.INVALID_REFERENCES);
    }


    private static String message(String type, String name)
    {
        return String.format("Attribute %s does not exist in type %s to be overridden", new Object[] {name, type});
    }
}

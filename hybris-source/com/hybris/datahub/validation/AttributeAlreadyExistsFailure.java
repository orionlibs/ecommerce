package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.AttributeData;

public class AttributeAlreadyExistsFailure extends AbstractAttributeValidationFailure
{
    public AttributeAlreadyExistsFailure(AttributeData data)
    {
        super(data, message(data.getItemType(), data.getName()), ValidationFailureType.EXISTING_ITEM);
    }


    private static String message(String type, String name)
    {
        return String.format("Attribute %s already exists in type %s", new Object[] {name, type});
    }
}

package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.AttributeData;

public class PropertyRequiredInAttributeDefinitionFailure extends AbstractAttributeValidationFailure
{
    public PropertyRequiredInAttributeDefinitionFailure(AttributeData d, String p)
    {
        super(d, p, message(d), ValidationFailureType.FATAL);
    }


    private static String message(AttributeData d)
    {
        return String.format("Field is required in definition of attribute %s in item type %s", new Object[] {d.getName(), d.getItemType()});
    }
}

package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.AttributeData;
import com.hybris.datahub.dto.metadata.CanonicalTransformationData;

public class DisabledTransformationDoesNotExistFailure extends AbstractAttributeValidationFailure
{
    public DisabledTransformationDoesNotExistFailure(CanonicalTransformationData data)
    {
        super((AttributeData)data, message(data), ValidationFailureType.INVALID_REFERENCES);
    }


    private static String message(CanonicalTransformationData d)
    {
        return String.format("Transformation from %s type to attribute %s in type %s does not exist to be disabled", new Object[] {d
                        .getRawItemType(), d.getName(), d.getItemType()});
    }
}

package com.hybris.datahub.validation;

import com.hybris.datahub.dto.metadata.AttributeData;
import com.hybris.datahub.dto.metadata.CanonicalTransformationData;

public class TransformationDisableOverrideFailure extends AbstractAttributeValidationFailure
{
    public TransformationDisableOverrideFailure(CanonicalTransformationData data)
    {
        super((AttributeData)data, "disabled", message(data), ValidationFailureType.FATAL);
    }


    private static String message(CanonicalTransformationData d)
    {
        return String.format("Transformation from %s type to attribute %s in type %s contains both 'override' and 'disable' attributes", new Object[] {d
                        .getRawItemType(), d.getName(), d.getItemType()});
    }
}

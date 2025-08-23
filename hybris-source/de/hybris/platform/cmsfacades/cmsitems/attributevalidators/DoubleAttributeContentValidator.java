/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributevalidators;

import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_NOT_DOUBLE;
import static java.lang.Double.parseDouble;

import de.hybris.platform.cmsfacades.validator.data.ValidationError;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Double validator adds validation errors when the value is not parsable into a Double
 */
public class DoubleAttributeContentValidator extends NumberAttributeContentValidator
{
    @Override
    public List<ValidationError> validate(final Object value, final AttributeDescriptorModel attribute)
    {
        final List<ValidationError> errors = new ArrayList<>();
        if(value == null)
        {
            return errors;
        }
        try
        {
            parseDouble(value.toString());
            errors.addAll(super.validate(value, attribute));
        }
        catch(NumberFormatException e)
        {
            errors.add(
                            newValidationErrorBuilder() //
                                            .field(attribute.getQualifier()) //
                                            .errorCode(FIELD_NOT_DOUBLE) //
                                            .build());
        }
        return errors;
    }
}

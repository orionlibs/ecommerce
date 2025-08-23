package de.hybris.platform.webservicescommons.validators;

import org.apache.commons.validator.routines.IntegerValidator;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class FieldIntegerValidator extends AbstractFieldValidator
{
    private static final String FIELD_INTEGER_MESSAGE_ID = "field.integer";
    private static final String FIELD_INTEGER_TEXT = "This field must be an integer.";


    public boolean supports(Class clazz)
    {
        return true;
    }


    public void validate(Object target, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        Object fieldValue = (getFieldPath() == null) ? target : errors.getFieldValue(getFieldPath());
        if(fieldValue instanceof Integer)
        {
            return;
        }
        if(fieldValue instanceof String && IntegerValidator.getInstance().isValid((String)fieldValue))
        {
            return;
        }
        errors.rejectValue(getFieldPath(), "field.integer", (Object[])new String[] {getFieldPath()}, "This field must be an integer.");
    }
}

package de.hybris.platform.webservicescommons.validators;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class FieldGreaterThanZeroValidator extends AbstractFieldValidator
{
    private static final String FIELD_GREATER_THAN_ZERO_MESSAGE_ID = "field.greaterThanZero";
    private static final String FIELD_GREATER_THAN_ZERO_MESSAGE_TEXT = "This field must be greater than 0.";


    public boolean supports(Class clazz)
    {
        return true;
    }


    public void validate(Object target, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        Object fieldValue = (getFieldPath() == null) ? target : errors.getFieldValue(getFieldPath());
        if(fieldValue instanceof Number)
        {
            validateNumber((Number)fieldValue, getFieldPath(), errors);
        }
        else if(fieldValue instanceof String)
        {
            validateString((String)fieldValue, getFieldPath(), errors);
        }
        else
        {
            errors.rejectValue(getFieldPath(), "field.greaterThanZero", (Object[])new String[] {getFieldPath()}, "This field must be greater than 0.");
        }
    }


    protected void validateNumber(Number value, String fieldPath, Errors errors)
    {
        if(value.longValue() <= 0L)
        {
            errors.rejectValue(fieldPath, "field.greaterThanZero", (Object[])new String[] {fieldPath}, "This field must be greater than 0.");
        }
    }


    protected void validateString(String value, String fieldPath, Errors errors)
    {
        try
        {
            if(Long.parseLong(value) <= 0L)
            {
                errors.rejectValue(fieldPath, "field.greaterThanZero", (Object[])new String[] {fieldPath}, "This field must be greater than 0.");
            }
        }
        catch(NumberFormatException e)
        {
            errors.rejectValue(fieldPath, "field.greaterThanZero", (Object[])new String[] {fieldPath}, "This field must be greater than 0.");
        }
    }
}

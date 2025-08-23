package de.hybris.platform.webservicescommons.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class FieldNotEmptyValidator extends AbstractFieldValidator
{
    private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";


    public boolean supports(Class clazz)
    {
        return true;
    }


    public void validate(Object object, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        Object fieldValue = errors.getFieldValue(getFieldPath());
        if(fieldValue == null || (fieldValue instanceof String && StringUtils.isBlank((String)fieldValue)))
        {
            errors.rejectValue(getFieldPath(), "field.required", (Object[])new String[] {getFieldPath()}, "This field is required.");
        }
    }
}

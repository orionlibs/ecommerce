package de.hybris.platform.webservicescommons.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class FieldEmptyValidator extends AbstractFieldValidator
{
    private static final String FIELD_NOT_ALLOWED_MESSAGE_ID = "field.not.allowed";


    public boolean supports(Class clazz)
    {
        return true;
    }


    public void validate(Object object, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        Object fieldValue = errors.getFieldValue(getFieldPath());
        if(fieldValue != null && fieldValue instanceof String && StringUtils.isNotBlank((String)fieldValue))
        {
            errors.rejectValue(getFieldPath(), "field.not.allowed", (Object[])new String[] {getFieldPath()}, null);
        }
    }
}

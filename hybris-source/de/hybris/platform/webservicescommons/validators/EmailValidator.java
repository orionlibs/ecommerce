package de.hybris.platform.webservicescommons.validators;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class EmailValidator extends AbstractFieldValidator
{
    private static final String INVALID_EMAIL_MESSAGE_ID = "field.invalidEmail";


    public boolean supports(Class<?> aClass)
    {
        return true;
    }


    public void validate(Object o, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        String fieldValue = (String)errors.getFieldValue(getFieldPath());
        if(!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(fieldValue))
        {
            errors.rejectValue(getFieldPath(), "field.invalidEmail", (Object[])new String[] {getFieldPath()}, "This field is not a valid email addresss.");
        }
    }
}

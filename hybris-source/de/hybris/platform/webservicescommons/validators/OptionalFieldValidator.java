package de.hybris.platform.webservicescommons.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OptionalFieldValidator extends AbstractFieldValidator
{
    private final Validator validator;


    public OptionalFieldValidator(Validator validator)
    {
        this.validator = validator;
    }


    public boolean supports(Class clazz)
    {
        return this.validator.supports(clazz);
    }


    public void validate(Object o, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        String fieldValue = (String)errors.getFieldValue(getFieldPath());
        if(StringUtils.isNotEmpty(fieldValue))
        {
            this.validator.validate(o, errors);
        }
    }
}

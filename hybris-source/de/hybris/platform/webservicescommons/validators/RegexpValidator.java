package de.hybris.platform.webservicescommons.validators;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class RegexpValidator extends AbstractFieldValidator
{
    private String regularExpression;
    private String errorMessageID;


    public boolean supports(Class<?> aClass)
    {
        return true;
    }


    public void validate(Object o, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        String fieldValue = (String)errors.getFieldValue(getFieldPath());
        RegexValidator validator = new RegexValidator(getRegularExpression());
        if(!validator.isValid(fieldValue))
        {
            errors.rejectValue(getFieldPath(), getErrorMessageID(), (Object[])new String[] {getFieldPath()}, null);
        }
    }


    @Required
    public void setRegularExpression(String regularExpression)
    {
        this.regularExpression = regularExpression;
    }


    public String getRegularExpression()
    {
        return this.regularExpression;
    }


    @Required
    public void setErrorMessageID(String errorMessageID)
    {
        this.errorMessageID = errorMessageID;
    }


    public String getErrorMessageID()
    {
        return this.errorMessageID;
    }
}

package de.hybris.platform.webservicescommons.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class FieldNotEmptyOrTooLongValidator extends AbstractFieldValidator
{
    private static final String FIELD_REQUIRED_AND_NOT_TOO_LONG_MESSAGE_ID = "field.requiredAndNotTooLong";
    private int maxLength;
    private String errorMessageId;


    public boolean supports(Class clazz)
    {
        return true;
    }


    public void validate(Object object, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        String fieldValue = (String)errors.getFieldValue(getFieldPath());
        String resultErrorMessageId = (this.errorMessageId != null) ? this.errorMessageId : "field.requiredAndNotTooLong";
        if(StringUtils.isBlank(fieldValue) || StringUtils.length(fieldValue) > this.maxLength)
        {
            errors.rejectValue(getFieldPath(), resultErrorMessageId, (Object[])new String[] {String.valueOf(this.maxLength)}, "This field is required and must to be between 1 and {0} characters long.");
        }
    }


    public int getMaxLength()
    {
        return this.maxLength;
    }


    @Required
    public void setMaxLength(int maxLength)
    {
        this.maxLength = maxLength;
    }


    public String getErrorMessageId()
    {
        return this.errorMessageId;
    }


    public void setErrorMessageId(String errorMessageId)
    {
        this.errorMessageId = errorMessageId;
    }
}

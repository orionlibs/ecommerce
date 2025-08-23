package de.hybris.platform.webservicescommons.validators;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EnumValueValidator implements Validator
{
    private static final String MESSAGE = "enum.invalidValue";
    public static final String DEFAULT_MESSAGE = "[{0}] is not valid value for [{1}]";
    private EnumerationService enumerationService;
    private final String enumClass;


    public EnumValueValidator(String enumClass)
    {
        this.enumClass = enumClass;
    }


    public boolean supports(Class<?> paramClass)
    {
        return (String[].class == paramClass);
    }


    public void validate(Object o, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        String[] param = (String[])o;
        for(String v : param)
        {
            validate(YSanitizer.sanitize(v), errors);
        }
    }


    protected void validate(String value, Errors errors)
    {
        Object result;
        try
        {
            result = this.enumerationService.getEnumerationValue(this.enumClass, value);
        }
        catch(UnknownIdentifierException e)
        {
            result = null;
        }
        if(result == null)
        {
            errors.reject("enum.invalidValue", (Object[])new String[] {value, this.enumClass}, "[{0}] is not valid value for [{1}]");
        }
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }
}

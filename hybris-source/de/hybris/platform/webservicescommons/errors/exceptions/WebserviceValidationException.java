package de.hybris.platform.webservicescommons.errors.exceptions;

public class WebserviceValidationException extends RuntimeException
{
    private final transient Object validationObject;


    public WebserviceValidationException(Object validationObject)
    {
        super("Validation error");
        this.validationObject = validationObject;
    }


    public Object getValidationObject()
    {
        return this.validationObject;
    }
}

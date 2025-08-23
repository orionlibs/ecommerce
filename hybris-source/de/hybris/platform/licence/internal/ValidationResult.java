package de.hybris.platform.licence.internal;

public class ValidationResult
{
    final boolean valid;
    final String message;


    public ValidationResult(boolean valid, String message)
    {
        this.valid = valid;
        this.message = message;
    }


    public boolean isValid()
    {
        return this.valid;
    }


    public String getMessage()
    {
        return this.message;
    }
}

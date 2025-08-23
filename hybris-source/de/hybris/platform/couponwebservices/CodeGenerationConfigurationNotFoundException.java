package de.hybris.platform.couponwebservices;

import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;

public class CodeGenerationConfigurationNotFoundException extends NotFoundException
{
    private static final String TYPE = "CodeGenerationConfigurationNotFoundError";


    public CodeGenerationConfigurationNotFoundException(String message, String reason, String subject, Throwable cause)
    {
        super(message, reason, subject, cause);
    }


    public CodeGenerationConfigurationNotFoundException(String message, String reason, String subject)
    {
        super(message, reason, subject);
    }


    public CodeGenerationConfigurationNotFoundException(String message, String reason, Throwable cause)
    {
        super(message, reason, cause);
    }


    public CodeGenerationConfigurationNotFoundException(String message, String reason)
    {
        super(message, reason);
    }


    public CodeGenerationConfigurationNotFoundException(String message)
    {
        super(message);
    }


    public String getType()
    {
        return "CodeGenerationConfigurationNotFoundError";
    }
}

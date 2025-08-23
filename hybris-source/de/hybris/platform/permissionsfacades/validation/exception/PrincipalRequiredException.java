package de.hybris.platform.permissionsfacades.validation.exception;

public class PrincipalRequiredException extends RuntimeException
{
    private static final String ERROR_MSG = "Attribute 'principalUid' is a required field.";


    public PrincipalRequiredException()
    {
        super("Attribute 'principalUid' is a required field.");
    }
}

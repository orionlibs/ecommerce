package de.hybris.platform.permissionsfacades.validation.exception;

public class PermissionsListRequiredException extends RuntimeException
{
    private static final String ERROR_MSG = "Attribute 'permissionsList' is a required field.";


    public PermissionsListRequiredException()
    {
        super("Attribute 'permissionsList' is a required field.");
    }
}

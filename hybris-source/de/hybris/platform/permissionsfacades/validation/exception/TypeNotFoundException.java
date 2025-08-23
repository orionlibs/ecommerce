package de.hybris.platform.permissionsfacades.validation.exception;

public class TypeNotFoundException extends RuntimeException
{
    private static final String ERROR_MSG = "Type %s is not found in the system.";


    public TypeNotFoundException(String type)
    {
        super(String.format("Type %s is not found in the system.", new Object[] {type}));
    }
}

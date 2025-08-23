package de.hybris.platform.permissionsfacades.validation.exception;

public class TypeRequiredException extends RuntimeException
{
    private static final String ERROR_MESSAGE = "Attribute 'type' is a required field for type permissions entry %d.";


    public TypeRequiredException(int index)
    {
        super(String.format("Attribute 'type' is a required field for type permissions entry %d.", new Object[] {Integer.valueOf(index)}));
    }
}

package de.hybris.platform.permissionsfacades.validation.exception;

public class TypeDuplicatedException extends RuntimeException
{
    private static final String ERROR_MESSAGE_TEMPLATE = "Type %s is duplicated.";


    public TypeDuplicatedException(String type)
    {
        super(String.format("Type %s is duplicated.", new Object[] {type}));
    }
}

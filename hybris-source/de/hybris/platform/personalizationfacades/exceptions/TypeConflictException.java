package de.hybris.platform.personalizationfacades.exceptions;

public class TypeConflictException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    public TypeConflictException(String message, Throwable rootCause)
    {
        super(message, rootCause);
    }


    public TypeConflictException(String message)
    {
        super(message);
    }
}

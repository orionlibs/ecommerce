package de.hybris.platform.servicelayer.exceptions;

public class ClassMismatchException extends SystemException
{
    public ClassMismatchException(Object expected, Object got)
    {
        super("Expected: " + expected.toString() + " but got: " + got.toString());
    }


    public ClassMismatchException(Object expected, Object got, Throwable cause)
    {
        super("Expected: " + expected + " but got: " + got, cause);
    }
}

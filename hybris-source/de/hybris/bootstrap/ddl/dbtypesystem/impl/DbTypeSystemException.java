package de.hybris.bootstrap.ddl.dbtypesystem.impl;

public class DbTypeSystemException extends IllegalStateException
{
    public DbTypeSystemException()
    {
    }


    public DbTypeSystemException(String message)
    {
        super(message);
    }


    public DbTypeSystemException(Throwable cause)
    {
        super(cause);
    }


    public DbTypeSystemException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

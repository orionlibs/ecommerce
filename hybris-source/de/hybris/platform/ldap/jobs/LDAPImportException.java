package de.hybris.platform.ldap.jobs;

public class LDAPImportException extends RuntimeException
{
    public LDAPImportException(String message)
    {
        super(message);
    }


    public LDAPImportException(Throwable cause)
    {
        super(cause);
    }


    public LDAPImportException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

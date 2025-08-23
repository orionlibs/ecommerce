package de.hybris.platform.ldap.exception;

public class LDAPOperationException extends Exception
{
    private int errorCode;


    public LDAPOperationException(String message)
    {
        super(message);
    }


    public LDAPOperationException(String message, int errCode)
    {
        super(message);
        this.errorCode = errCode;
    }


    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }


    public int getErrorCode()
    {
        return this.errorCode;
    }


    public static LDAPOperationException createLDAPException(String message)
    {
        LDAPOperationException newException = null;
        if(message.contains("-601"))
        {
            LDAPNoSuchEntryException lDAPNoSuchEntryException = new LDAPNoSuchEntryException(message);
        }
        else
        {
            newException = new LDAPOperationException(message);
        }
        StackTraceElement[] elements = newException.getStackTrace();
        if(elements != null && elements.length > 1)
        {
            StackTraceElement[] newElements = new StackTraceElement[elements.length - 1];
            System.arraycopy(elements, 1, newElements, 0, elements.length - 1);
            newException.setStackTrace(newElements);
        }
        return newException;
    }
}

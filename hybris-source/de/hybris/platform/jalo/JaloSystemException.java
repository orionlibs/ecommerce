package de.hybris.platform.jalo;

public class JaloSystemException extends RuntimeException
{
    private final int errorCode;


    public JaloSystemException(Throwable nested, String message, int errorCode)
    {
        super(message, nested);
        this.errorCode = errorCode;
    }


    public JaloSystemException(String message, int errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }


    public JaloSystemException(Throwable nested, int errorCode)
    {
        super(nested.getMessage(), nested);
        this.errorCode = errorCode;
    }


    public JaloSystemException(Throwable nested)
    {
        super(nested.getMessage(), nested);
        this.errorCode = -1;
    }


    public JaloSystemException(String message)
    {
        super(message);
        this.errorCode = -1;
    }


    public int getErrorCode()
    {
        return this.errorCode;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Throwable getThrowable()
    {
        return getCause();
    }


    public String toString()
    {
        return super.toString() + "[HY-" + super.toString() + "]";
    }
}

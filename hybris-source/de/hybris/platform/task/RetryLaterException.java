package de.hybris.platform.task;

public class RetryLaterException extends RuntimeException
{
    public static final long DEFAULT_DELAY = 300000L;
    private long delay = 300000L;
    private Method method = Method.EXPONENTIAL;
    private boolean RollBack = true;


    public RetryLaterException()
    {
        this(null);
    }


    public RetryLaterException(String msg)
    {
        this(msg, null);
    }


    public RetryLaterException(String msg, Throwable cause)
    {
        super(msg, cause);
    }


    public long getDelay()
    {
        return this.delay;
    }


    public void setDelay(long delay)
    {
        if(delay <= 0L)
        {
            throw new IllegalArgumentException("Delay must be positive.");
        }
        this.delay = delay;
    }


    public Method getMethod()
    {
        return this.method;
    }


    public void setMethod(Method method)
    {
        if(method == null)
        {
            throw new IllegalArgumentException("Method must not be null.");
        }
        this.method = method;
    }


    public void setRollBack(boolean rollBack)
    {
        this.RollBack = rollBack;
    }


    public boolean isRollBack()
    {
        return this.RollBack;
    }
}

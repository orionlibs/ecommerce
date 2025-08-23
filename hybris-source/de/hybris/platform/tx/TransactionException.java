package de.hybris.platform.tx;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.apache.log4j.Logger;

public class TransactionException extends RuntimeException
{
    static final Logger log = Logger.getLogger(TransactionException.class.getName());
    private final Throwable throwable;
    private final int vendorCode;


    public TransactionException(Throwable throwable, String message, int vendorCode)
    {
        super(message, throwable);
        this.throwable = throwable;
        this.vendorCode = vendorCode;
    }


    public TransactionException(String message)
    {
        this(null, message, -1);
    }


    public TransactionException(Throwable throwable)
    {
        this(throwable, throwable.getMessage(), -1);
    }


    public int getErrorCode()
    {
        return this.vendorCode;
    }


    public Throwable getThrowable()
    {
        return this.throwable;
    }


    public String getMessage()
    {
        return super.getMessage();
    }


    public String toString()
    {
        String s = getClass().getName();
        s = s + ": ";
        if(getMessage() != null)
        {
            s = s + s;
        }
        s = s + "[VC:" + s + "]";
        if(getThrowable() != null)
        {
            s = s + "\nNESTED: " + s;
        }
        return s;
    }


    public void printStackTrace()
    {
        super.printStackTrace();
        if(this.throwable != null)
        {
            log.error("NESTED EXCEPTION:");
            this.throwable.printStackTrace();
        }
    }


    public void printStackTrace(PrintWriter pw)
    {
        super.printStackTrace(pw);
        if(this.throwable != null)
        {
            pw.println("NESTED EXCEPTION:");
            this.throwable.printStackTrace(pw);
        }
    }


    public void printStackTrace(PrintStream ps)
    {
        super.printStackTrace(ps);
        if(this.throwable != null)
        {
            ps.println("NESTED EXCEPTION:");
            this.throwable.printStackTrace(ps);
        }
    }
}

package de.hybris.platform.persistence.hjmpgen;

import java.io.PrintStream;
import java.io.PrintWriter;

public class HJMPGeneratorException extends RuntimeException
{
    final Exception rootCause;


    public HJMPGeneratorException(Exception nested)
    {
        super(nested.getMessage());
        this.rootCause = nested;
    }


    public HJMPGeneratorException(String msg)
    {
        super(msg);
        this.rootCause = null;
    }


    public Exception getCausedByException()
    {
        return this.rootCause;
    }


    public void printStackTrace(PrintStream ps)
    {
        super.printStackTrace(ps);
        if(getCausedByException() != null)
        {
            ps.println("NESTED EXCEPTION:");
            getCausedByException().printStackTrace(ps);
        }
    }


    public void printStackTrace(PrintWriter pw)
    {
        super.printStackTrace(pw);
        if(getCausedByException() != null)
        {
            pw.println("NESTED EXCEPTION:");
            getCausedByException().printStackTrace(pw);
        }
    }
}

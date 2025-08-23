package de.hybris.platform.persistence.hjmp;

import de.hybris.platform.util.jeeapi.YFinderException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class HJMPFinderException extends YFinderException
{
    final Exception rootCause;


    public HJMPFinderException(String msg)
    {
        super(msg);
        this.rootCause = null;
    }


    public HJMPFinderException(Exception nested)
    {
        super(nested.getMessage());
        this.rootCause = nested;
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

package de.hybris.platform.persistence.hjmp;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.util.jeeapi.YEJBException;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.springframework.dao.DataAccessException;

public class HJMPException extends YEJBException
{
    private ItemDeployment deployment;
    String theMessage = null;


    public HJMPException(String msg, Exception nested)
    {
        super(nested);
        this.theMessage = msg;
    }


    public HJMPException(String msg)
    {
        super(msg);
    }


    public HJMPException(Exception nested)
    {
        super(nested);
    }


    public HJMPException(DataAccessException e, ItemDeployment deployment)
    {
        super((Exception)e);
        this.deployment = deployment;
    }


    public String getMessage()
    {
        if(this.theMessage == null)
        {
            return super.getMessage();
        }
        return this.theMessage;
    }


    public void printStackTrace()
    {
        printStackTrace(System.err);
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


    public ItemDeployment getDeployment()
    {
        return this.deployment;
    }
}

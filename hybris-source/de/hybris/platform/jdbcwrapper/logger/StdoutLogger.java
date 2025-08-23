package de.hybris.platform.jdbcwrapper.logger;

import de.hybris.platform.core.Tenant;
import java.io.PrintStream;

public class StdoutLogger extends FormattedLogger
{
    protected PrintStream qlog = System.out;


    public StdoutLogger(Tenant system)
    {
    }


    public void logException(Exception exception)
    {
        exception.printStackTrace(this.qlog);
    }


    public void logText(String text)
    {
        this.qlog.println(text);
        setLastEntry(text);
    }
}

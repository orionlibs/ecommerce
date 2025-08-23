package de.hybris.platform.jdbcwrapper.logger;

import de.hybris.platform.core.Tenant;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class FileLogger extends StdoutLogger
{
    private final boolean append;


    public FileLogger(Tenant tenant)
    {
        super(tenant);
        this.append = tenant.getConfig().getBoolean("db.log.file.append", true);
    }


    public void setLogfile(String fileName)
    {
        if(fileName == null || fileName.length() == 0)
        {
            this.qlog.flush();
            this.qlog.close();
        }
        else
        {
            try
            {
                this.qlog = new PrintStream(new FileOutputStream(fileName, this.append));
            }
            catch(IOException e)
            {
                e.printStackTrace(System.err);
            }
        }
    }
}

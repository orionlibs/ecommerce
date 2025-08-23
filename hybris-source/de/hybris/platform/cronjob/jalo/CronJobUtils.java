package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.util.JspContext;
import java.io.IOException;
import org.apache.log4j.Logger;

public final class CronJobUtils
{
    private static final Logger LOG = Logger.getLogger(CronJobUtils.class);
    private static final ThreadLocal<Boolean> WRITING_LOG_TO_DB = ThreadLocal.withInitial(() -> Boolean.FALSE);


    public static boolean isWritingLogToDb()
    {
        return ((Boolean)WRITING_LOG_TO_DB.get()).booleanValue();
    }


    public static void startWritingLogToDb()
    {
        WRITING_LOG_TO_DB.set(Boolean.TRUE);
    }


    public static void stopWritingLogToDb()
    {
        WRITING_LOG_TO_DB.set(Boolean.FALSE);
    }


    public static void sendMsgToJsp(String msg, JspContext jspc)
    {
        try
        {
            if(jspc != null)
            {
                jspc.getJspWriter().print(msg);
                jspc.getJspWriter().flush();
            }
        }
        catch(IOException e)
        {
            LOG.error("Error while writing to jsp", e);
        }
    }
}

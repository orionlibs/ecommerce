package de.hybris.platform.patches.internal.logger.impl;

import de.hybris.platform.patches.internal.JspContextHolder;
import de.hybris.platform.patches.internal.logger.PatchLogger;
import de.hybris.platform.util.JspContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class PatchLoggerImpl implements PatchLogger
{
    private final Logger log;


    public PatchLoggerImpl(Class<?> clazz)
    {
        this.log = LoggerFactory.getLogger(clazz);
    }


    public String getName()
    {
        return this.log.getName();
    }


    public boolean isErrorEnabled()
    {
        return this.log.isErrorEnabled();
    }


    public void error(PatchLogger.LoggingMode loggingMode, String msg)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.error(msg);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logErrorMessageInJspContext(msg);
        }
    }


    public void error(PatchLogger.LoggingMode loggingMode, String msg, Object arg)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.error(msg, arg);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logErrorMessageInJspContext(MessageFormatter.format(msg, arg).getMessage());
        }
    }


    public void error(PatchLogger.LoggingMode loggingMode, String msg, Object arg1, Object arg2)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.error(msg, arg1, arg2);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logErrorMessageInJspContext(MessageFormatter.format(msg, arg1, arg2).getMessage());
        }
    }


    public void error(PatchLogger.LoggingMode loggingMode, String msg, Object... objects)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.error(msg, objects);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logErrorMessageInJspContext(MessageFormatter.arrayFormat(msg, objects).getMessage());
        }
    }


    public void error(PatchLogger.LoggingMode loggingMode, String msg, Throwable throwable)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.error(msg, throwable);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logErrorMessageInJspContext(msg);
        }
    }


    public void error(String msg, Throwable throwable)
    {
        this.log.error(msg, throwable);
    }


    public boolean isWarnEnabled()
    {
        return this.log.isWarnEnabled();
    }


    public void warn(PatchLogger.LoggingMode loggingMode, String msg)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.warn(msg);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logWarnMessageInJspContext(msg);
        }
    }


    public void warn(PatchLogger.LoggingMode loggingMode, String msg, Object arg)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.warn(msg, arg);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logWarnMessageInJspContext(MessageFormatter.format(msg, arg).getMessage());
        }
    }


    public void warn(PatchLogger.LoggingMode loggingMode, String msg, Object arg1, Object arg2)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.warn(msg, arg1, arg2);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logWarnMessageInJspContext(MessageFormatter.format(msg, arg1, arg2).getMessage());
        }
    }


    public void warn(PatchLogger.LoggingMode loggingMode, String msg, Object... objects)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.warn(msg, objects);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logWarnMessageInJspContext(MessageFormatter.arrayFormat(msg, objects).getMessage());
        }
    }


    public void warn(PatchLogger.LoggingMode loggingMode, String msg, Throwable throwable)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.warn(msg, throwable);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logWarnMessageInJspContext(msg);
        }
    }


    public boolean isInfoEnabled()
    {
        return this.log.isInfoEnabled();
    }


    public void info(PatchLogger.LoggingMode loggingMode, String msg)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.info(msg);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logInfoMessageInJspContext(msg);
        }
    }


    public void info(String msg)
    {
        this.log.info(msg);
    }


    public void info(PatchLogger.LoggingMode loggingMode, String msg, Object arg)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.info(msg, arg);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logInfoMessageInJspContext(MessageFormatter.format(msg, arg).getMessage());
        }
    }


    public void info(PatchLogger.LoggingMode loggingMode, String msg, Object arg1, Object arg2)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.info(msg, arg1, arg2);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logInfoMessageInJspContext(MessageFormatter.format(msg, arg1, arg2).getMessage());
        }
    }


    public void info(PatchLogger.LoggingMode loggingMode, String msg, Object... objects)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.info(msg, objects);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logInfoMessageInJspContext(MessageFormatter.arrayFormat(msg, objects).getMessage());
        }
    }


    public void info(PatchLogger.LoggingMode loggingMode, String msg, Throwable throwable)
    {
        if(loggingMode != PatchLogger.LoggingMode.HAC)
        {
            this.log.info(msg, throwable);
        }
        if(loggingMode != PatchLogger.LoggingMode.CONSOLE)
        {
            logInfoMessageInJspContext(msg);
        }
    }


    public boolean isDebugEnabled()
    {
        return this.log.isDebugEnabled();
    }


    public void debug(String msg)
    {
        this.log.debug(msg);
    }


    public void debug(String msg, Object arg)
    {
        this.log.debug(msg, arg);
    }


    public void debug(String msg, Object arg1, Object arg2)
    {
        this.log.debug(msg, arg1, arg2);
    }


    public void debug(String msg, Object... objects)
    {
        this.log.debug(msg, objects);
    }


    public void debug(String msg, Throwable throwable)
    {
        this.log.debug(msg, throwable);
    }


    private void logInfoMessageInJspContext(String message)
    {
        logMessageInJspContext(message);
    }


    private void logWarnMessageInJspContext(String message)
    {
        logMessageInJspContext(message);
    }


    private void logErrorMessageInJspContext(String message)
    {
        logMessageInJspContext("<span style='color:#c00'>" + message + "</span>");
    }


    private void logMessageInJspContext(String message)
    {
        JspContext jspContext = JspContextHolder.getJspContext();
        if(jspContext != null)
        {
            jspContext.println(message);
        }
    }
}

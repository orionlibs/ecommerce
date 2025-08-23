package de.hybris.platform.util.logging;

import org.apache.log4j.Category;
import org.apache.log4j.Level;

public class HybrisLoggingEvent
{
    private boolean denied;
    private transient Object message;
    private String renderedMessage;
    private final String fqnOfCategoryClass;
    private final Category logger;
    private final Level level;
    private final Throwable throwable;


    public HybrisLoggingEvent(String fqnOfCategoryClass, Category logger, Level level, Object message, Throwable throwable)
    {
        this.denied = false;
        this.message = message;
        this.fqnOfCategoryClass = fqnOfCategoryClass;
        this.logger = logger;
        this.level = level;
        this.throwable = throwable;
    }


    public void deny()
    {
        this.denied = true;
    }


    public boolean isDenied()
    {
        return this.denied;
    }


    public Object getMessage()
    {
        return this.message;
    }


    public void setMessage(Object message)
    {
        this.message = message;
    }


    public String getRenderedMessage()
    {
        if(this.renderedMessage == null && this.message != null)
        {
            if(this.message instanceof String)
            {
                this.renderedMessage = (String)this.message;
            }
            else
            {
                this.renderedMessage = this.message.toString();
            }
        }
        return this.renderedMessage;
    }


    public Level getLevel()
    {
        return this.level;
    }


    public String getFQNOfLoggerClass()
    {
        return this.fqnOfCategoryClass;
    }


    public Category getLogger()
    {
        return this.logger;
    }


    public Throwable getThrowable()
    {
        return this.throwable;
    }
}

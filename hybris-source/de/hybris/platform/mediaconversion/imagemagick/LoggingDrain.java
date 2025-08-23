package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.platform.mediaconversion.os.Drain;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class LoggingDrain implements Drain
{
    private final Logger logger;
    private final Level level;


    public LoggingDrain(Class<?> clazz, Level level)
    {
        this(clazz.getName(), level);
    }


    public LoggingDrain(String name, Level level)
    {
        this(Logger.getLogger(name), level);
    }


    public LoggingDrain(Logger logger, Level level)
    {
        this.logger = logger;
        this.level = level;
    }


    public void drain(String message)
    {
        getLogger().log((Priority)getLevel(), message);
    }


    public Logger getLogger()
    {
        return this.logger;
    }


    public Level getLevel()
    {
        return this.level;
    }
}

package de.hybris.platform.util.logging.log4j2;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.util.logging.HybrisLogger;
import de.hybris.platform.util.logging.HybrisLoggingEvent;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;

public class HybrisLog4j2Logger extends Logger
{
    private static final String FQCN = HybrisLogger.class.getName();
    private static final Map<Level, Level> NEW_TO_OLD_LEVEL_MAPPING = createV1ToV2LevelMapping();
    private static final Map<Level, Level> OLD_TO_NEW_LEVEL_MAPPING = createV2ToV1LevelMapping();
    private static final Level[] LOG_LEVELS_V2 = new Level[] {Level.OFF, Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.TRACE, Level.ALL};


    protected HybrisLog4j2Logger(LoggerContext context, String name, MessageFactory messageFactory)
    {
        super(context, name, messageFactory);
    }


    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t)
    {
        HybrisLoggingEvent event = new HybrisLoggingEvent(FQCN, null, mapV2ToV1Level(level), message.getFormattedMessage(), t);
        event = HybrisLogger.extendByFilters(event);
        if(event.isDenied())
        {
            return;
        }
        HybrisLogger.logToListeners(event);
        SimpleMessage simpleMessage = (event.getMessage() == null) ? null : new SimpleMessage(event.getMessage().toString());
        super.logMessage(fqcn, level, marker, (Message)simpleMessage, t);
    }


    protected void log(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable)
    {
        HybrisLoggingEvent event = new HybrisLoggingEvent(FQCN, null, mapV2ToV1Level(level), message.getFormattedMessage(), throwable);
        event = HybrisLogger.extendByFilters(event);
        if(event.isDenied())
        {
            return;
        }
        HybrisLogger.logToListeners(event);
        SimpleMessage simpleMessage = (event.getMessage() == null) ? null : new SimpleMessage(event.getMessage().toString());
        super.log(level, marker, fqcn, location, (Message)simpleMessage, throwable);
    }


    private static Map<Level, Level> createV1ToV2LevelMapping()
    {
        return (Map<Level, Level>)ImmutableMap.builder()
                        .put(Level.OFF, Level.OFF)
                        .put(Level.FATAL, Level.FATAL)
                        .put(Level.ERROR, Level.ERROR)
                        .put(Level.WARN, Level.WARN)
                        .put(Level.INFO, Level.INFO)
                        .put(Level.DEBUG, Level.DEBUG)
                        .put(Level.TRACE, Level.TRACE)
                        .put(Level.ALL, Level.ALL).build();
    }


    private static Map<Level, Level> createV2ToV1LevelMapping()
    {
        return (Map<Level, Level>)createV1ToV2LevelMapping().entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }


    public static Level mapV2ToV1Level(Level level)
    {
        Level levelV1 = NEW_TO_OLD_LEVEL_MAPPING.get(level);
        if(levelV1 != null)
        {
            return levelV1;
        }
        Level foundStandardLevel = getNearestTopStandardLevelForCustomLevel(level.intLevel());
        return NEW_TO_OLD_LEVEL_MAPPING.get(foundStandardLevel);
    }


    private static Level getNearestTopStandardLevelForCustomLevel(int intLevel)
    {
        if(intLevel > Level.OFF.intLevel() && intLevel < Level.FATAL.intLevel())
        {
            return Level.FATAL;
        }
        Level level = Level.OFF;
        for(Level lvl : LOG_LEVELS_V2)
        {
            if(lvl.intLevel() > intLevel)
            {
                break;
            }
            level = lvl;
        }
        return level;
    }


    public static Level mapV1ToV2Level(Level level)
    {
        return OLD_TO_NEW_LEVEL_MAPPING.get(level);
    }
}

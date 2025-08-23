package de.hybris.platform.util.logging;

import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.logging.log4j2.HybrisLog4j2Logger;
import de.hybris.platform.util.logging.log4j2.HybrisLoggerContext;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Priority;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.spi.LocationAwareLogger;

public class HybrisLogger extends Logger
{
    private static final String FQCN = HybrisLogger.class.getName();
    protected final Logger slf4jLogger;
    private final LocationAwareLogger locationAwareLogger;
    private static final Marker FATAL_MARKER = MarkerFactory.getMarker("FATAL");
    private final String name;
    private static final List<HybrisLogListener> listeners = new CopyOnWriteArrayList<>();
    private static final ThreadLocal<Boolean> currentlyProcessingListeners = new ThreadLocal<>();
    private static volatile boolean listenersDisabled = false;
    private static final List<HybrisLogFilter> filter = new CopyOnWriteArrayList<>();


    protected HybrisLogger(String name)
    {
        super(name);
        this.name = name;
        this.slf4jLogger = LoggerFactory.getLogger(name);
        if(this.slf4jLogger instanceof LocationAwareLogger)
        {
            this.locationAwareLogger = (LocationAwareLogger)this.slf4jLogger;
        }
        else
        {
            this.locationAwareLogger = null;
        }
    }


    public static void disableListeners()
    {
        listenersDisabled = true;
    }


    public static void enableListeners()
    {
        listenersDisabled = false;
    }


    public static void addFilter(HybrisLogFilter newFilter)
    {
        filter.add(newFilter);
    }


    public static void addFilterFirst(HybrisLogFilter newFilter)
    {
        filter.add(0, newFilter);
    }


    public static boolean removeFilter(HybrisLogFilter toRemove)
    {
        return filter.remove(toRemove);
    }


    public static HybrisLoggingEvent extendByFilters(HybrisLoggingEvent event)
    {
        if(!filter.isEmpty() && !Transaction.isInCommitOrRollback())
        {
            HybrisLoggingEvent ret = event;
            for(HybrisLogFilter inst : filter)
            {
                ret = inst.filterEvent(event);
                if(ret.isDenied())
                {
                    return ret;
                }
            }
            return ret;
        }
        return event;
    }


    public static void addListener(HybrisLogListener newListener)
    {
        if(listeners.contains(newListener))
        {
            System.err.println("Log listener " + newListener + " already registered!");
            Thread.dumpStack();
        }
        else
        {
            listeners.add(newListener);
        }
    }


    public static boolean removeListener(HybrisLogListener toRemove)
    {
        return listeners.remove(toRemove);
    }


    public static void removeAllListeners()
    {
        listeners.clear();
    }


    public static List<HybrisLogListener> getAllAListeners()
    {
        return Collections.unmodifiableList(listeners);
    }


    protected boolean existsListenerEnabledFor(Priority level)
    {
        if(!listeners.isEmpty() && !listenersDisabled && !Transaction.isInCommitOrRollback())
        {
            Level lev = (Level)level;
            for(HybrisLogListener l : listeners)
            {
                try
                {
                    if(l.isEnabledFor(lev))
                    {
                        return true;
                    }
                }
                catch(Exception e)
                {
                    System.err.println("error checking listener " + l + " : " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }


    public static void logToListeners(HybrisLoggingEvent event)
    {
        if(!isThreadCurrentlyLoggingToListeners() && !listeners.isEmpty() && !listenersDisabled && !Transaction.isInCommitOrRollback())
        {
            Level lev = event.getLevel();
            markThreadCurrentlyLoggingToListeners();
            try
            {
                for(HybrisLogListener lsn : listeners)
                {
                    try
                    {
                        if(lsn.isEnabledFor(lev))
                        {
                            lsn.log(event);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println("error logging to listener: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            finally
            {
                unsetThreadCurrentlyLoggingToListeners();
            }
        }
    }


    protected static boolean isThreadCurrentlyLoggingToListeners()
    {
        return Boolean.TRUE.equals(currentlyProcessingListeners.get());
    }


    protected static void markThreadCurrentlyLoggingToListeners()
    {
        currentlyProcessingListeners.set(Boolean.TRUE);
    }


    protected static void unsetThreadCurrentlyLoggingToListeners()
    {
        currentlyProcessingListeners.remove();
    }


    public void debug(Object message)
    {
        bridgeLogMsg(null, FQCN, 10, message, null);
    }


    public void debug(Object message, Throwable throwable)
    {
        bridgeLogMsg(null, FQCN, 10, message, throwable);
    }


    public void error(Object message)
    {
        bridgeLogMsg(null, FQCN, 40, message, null);
    }


    public void error(Object message, Throwable throwable)
    {
        bridgeLogMsg(null, FQCN, 40, message, throwable);
    }


    public void fatal(Object message)
    {
        bridgeLogMsg(FATAL_MARKER, FQCN, 40, message, null);
    }


    public void fatal(Object message, Throwable throwable)
    {
        bridgeLogMsg(FATAL_MARKER, FQCN, 40, message, throwable);
    }


    public void info(Object message)
    {
        bridgeLogMsg(null, FQCN, 20, message, null);
    }


    public void warn(Object message, Throwable throwable)
    {
        bridgeLogMsg(null, FQCN, 30, message, throwable);
    }


    public void warn(Object message)
    {
        bridgeLogMsg(null, FQCN, 30, message, null);
    }


    public void info(Object message, Throwable throwable)
    {
        bridgeLogMsg(null, FQCN, 20, message, throwable);
    }


    private String convertToString(Object message)
    {
        if(message == null)
        {
            return (String)message;
        }
        return message.toString();
    }


    private int mapPriorityToInt(Priority level)
    {
        int mappedLevel = 0;
        switch(level.toInt())
        {
            case 10000:
                mappedLevel = 10;
                break;
            case 20000:
                mappedLevel = 20;
                break;
            case 30000:
                mappedLevel = 30;
                break;
            case 40000:
                mappedLevel = 40;
                break;
            case 50000:
                mappedLevel = 40;
                break;
        }
        return mappedLevel;
    }


    public void log(String callerFQCN, Priority level, Object message, Throwable t)
    {
        int mappedLevel = mapPriorityToInt(level);
        bridgeLogMsg(null, callerFQCN, mappedLevel, message, t);
    }


    public void log(Priority priority, Object message)
    {
        int mappedLevel = mapPriorityToInt(priority);
        bridgeLogMsg(null, FQCN, mappedLevel, message, null);
    }


    public void log(Priority priority, Object message, Throwable t)
    {
        int mappedLevel = mapPriorityToInt(priority);
        bridgeLogMsg(null, FQCN, mappedLevel, message, t);
    }


    void bridgeLogMsg(Marker marker, String fqcn, int level, Object message, Throwable t)
    {
        String m = convertToString(message);
        if(this.locationAwareLogger != null)
        {
            this.locationAwareLogger.log(marker, fqcn, level, m, null, t);
        }
        else
        {
            switch(level)
            {
                case 0:
                    this.slf4jLogger.trace(marker, m);
                    break;
                case 10:
                    this.slf4jLogger.debug(marker, m);
                    break;
                case 20:
                    this.slf4jLogger.info(marker, m);
                    break;
                case 30:
                    this.slf4jLogger.warn(marker, m);
                    break;
                case 40:
                    this.slf4jLogger.error(marker, m);
                    break;
            }
        }
    }


    public boolean isDebugEnabled()
    {
        return (this.slf4jLogger.isDebugEnabled() || existsListenerEnabledFor((Priority)Level.DEBUG));
    }


    public boolean isEnabledFor(Priority level)
    {
        if(level.equals(Level.TRACE))
        {
            return this.slf4jLogger.isTraceEnabled();
        }
        if(level.equals(Level.DEBUG))
        {
            return this.slf4jLogger.isDebugEnabled();
        }
        if(level.equals(Level.INFO))
        {
            return this.slf4jLogger.isInfoEnabled();
        }
        if(level.equals(Level.WARN))
        {
            return this.slf4jLogger.isWarnEnabled();
        }
        return this.slf4jLogger.isErrorEnabled();
    }


    public void setLevel(Level level)
    {
        HybrisLoggerContext ctx = (HybrisLoggerContext)LogManager.getContext(false);
        LoggerConfig loggerConfig = ctx.getConfiguration().getLoggerConfig(this.name);
        if(this.name.equals(loggerConfig.getName()))
        {
            loggerConfig.setLevel(HybrisLog4j2Logger.mapV1ToV2Level(level));
        }
        else
        {
            Level level1 = HybrisLog4j2Logger.mapV1ToV2Level(level);
            LoggerConfig config = new LoggerConfig(this.name, level1, false);
            ctx.getConfiguration().addLogger(this.name, config);
        }
        ctx.updateLoggers();
    }


    public boolean isInfoEnabled()
    {
        return (this.slf4jLogger.isInfoEnabled() || existsListenerEnabledFor((Priority)Level.INFO));
    }


    public static List<HybrisLogFilter> getFilters()
    {
        return filter;
    }
}

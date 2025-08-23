package com.hybris.datahub.log;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public final class Log implements Logger
{
    private final Logger logger;


    Log(Logger l)
    {
        this.logger = l;
    }


    public static Log getLogger(Class<?> cl)
    {
        Logger l = LoggerFactory.getLogger(cl);
        return new Log(l);
    }


    public static Log getLogger(String name)
    {
        Logger l = LoggerFactory.getLogger(name);
        return new Log(l);
    }


    public String getName()
    {
        return this.logger.getName();
    }


    public boolean isTraceEnabled()
    {
        return this.logger.isTraceEnabled();
    }


    public void trace(String s)
    {
        this.logger.trace(s);
    }


    public void trace(String s, Object o)
    {
        this.logger.trace(s, o);
    }


    public void trace(String s, Object o, Object o1)
    {
        this.logger.trace(s, o, o1);
    }


    public void trace(String s, Object... objects)
    {
        this.logger.trace(s, objects);
    }


    public void trace(String s, Throwable throwable)
    {
        this.logger.trace(s, throwable);
    }


    public boolean isTraceEnabled(Marker marker)
    {
        return this.logger.isTraceEnabled(marker);
    }


    public void trace(Marker marker, String s)
    {
        this.logger.trace(marker, s);
    }


    public void trace(Marker marker, String s, Object o)
    {
        this.logger.trace(marker, s, o);
    }


    public void trace(Marker marker, String s, Object o, Object o1)
    {
        this.logger.trace(marker, s, o, o1);
    }


    public void trace(Marker marker, String s, Object... objects)
    {
        this.logger.trace(marker, s, objects);
    }


    public void trace(Marker marker, String s, Throwable throwable)
    {
        this.logger.trace(marker, s, throwable);
    }


    public boolean isDebugEnabled()
    {
        return this.logger.isDebugEnabled();
    }


    public void debug(String s)
    {
        this.logger.debug(s);
    }


    public void debug(String s, Object o)
    {
        this.logger.debug(s, o);
    }


    public void debug(String s, Object o, Object o1)
    {
        this.logger.debug(s, o, o1);
    }


    public void debug(String s, Object... objects)
    {
        this.logger.debug(s, objects);
    }


    public void debug(String s, Throwable throwable)
    {
        this.logger.debug(s, throwable);
    }


    public boolean isDebugEnabled(Marker marker)
    {
        return this.logger.isDebugEnabled(marker);
    }


    public void debug(Marker marker, String s)
    {
        this.logger.debug(marker, s);
    }


    public void debug(Marker marker, String s, Object o)
    {
        this.logger.debug(marker, s, o);
    }


    public void debug(Marker marker, String s, Object o, Object o1)
    {
        this.logger.debug(marker, s, o, o1);
    }


    public void debug(Marker marker, String s, Object... objects)
    {
        this.logger.debug(marker, s, objects);
    }


    public void debug(Marker marker, String s, Throwable throwable)
    {
        this.logger.debug(marker, s, throwable);
    }


    public boolean isInfoEnabled()
    {
        return this.logger.isInfoEnabled();
    }


    public void info(String s)
    {
        this.logger.info(s);
    }


    public void info(String s, Object o)
    {
        this.logger.info(s, o);
    }


    public void info(String s, Object o, Object o1)
    {
        Object[] params = {o, o1};
        info(s, params);
    }


    public void info(String s, Object... objects)
    {
        Object[] params = stackTrackSafeParams(objects);
        this.logger.info(s, params);
    }


    public void info(String s, Throwable throwable)
    {
        Object[] params = {throwable};
        info(s, params);
    }


    public boolean isInfoEnabled(Marker marker)
    {
        return this.logger.isInfoEnabled(marker);
    }


    public void info(Marker marker, String s)
    {
        this.logger.info(marker, s);
    }


    public void info(Marker marker, String s, Object o)
    {
        this.logger.info(marker, s, o);
    }


    public void info(Marker marker, String s, Object o, Object o1)
    {
        Object[] params = {o, o1};
        info(marker, s, params);
    }


    public void info(Marker marker, String s, Object... objects)
    {
        Object[] params = stackTrackSafeParams(objects);
        this.logger.info(marker, s, params);
    }


    public void info(Marker marker, String s, Throwable throwable)
    {
        Object[] params = {throwable};
        info(marker, s, params);
    }


    public boolean isWarnEnabled()
    {
        return this.logger.isWarnEnabled();
    }


    public void warn(String s)
    {
        this.logger.warn(s);
    }


    public void warn(String s, Object o)
    {
        this.logger.warn(s, o);
    }


    public void warn(String s, Object... objects)
    {
        Object[] params = stackTrackSafeParams(objects);
        this.logger.warn(s, params);
    }


    public void warn(String s, Object o, Object o1)
    {
        Object[] params = {o, o1};
        warn(s, params);
    }


    public void warn(String s, Throwable throwable)
    {
        Object[] params = {throwable};
        warn(s, params);
    }


    public boolean isWarnEnabled(Marker marker)
    {
        return this.logger.isWarnEnabled(marker);
    }


    public void warn(Marker marker, String s)
    {
        this.logger.warn(marker, s);
    }


    public void warn(Marker marker, String s, Object o)
    {
        this.logger.warn(marker, s, o);
    }


    public void warn(Marker marker, String s, Object o, Object o1)
    {
        Object[] params = {o, o1};
        warn(marker, s, params);
    }


    public void warn(Marker marker, String s, Object... objects)
    {
        Object[] params = stackTrackSafeParams(objects);
        this.logger.warn(marker, s, params);
    }


    public void warn(Marker marker, String s, Throwable throwable)
    {
        Object[] params = {throwable};
        warn(marker, s, params);
    }


    public boolean isErrorEnabled()
    {
        return this.logger.isErrorEnabled();
    }


    public void error(String s)
    {
        this.logger.error(s);
    }


    public void error(String s, Object o)
    {
        this.logger.error(s, o);
    }


    public void error(String s, Object o, Object o1)
    {
        Object[] params = {o, o1};
        error(s, params);
    }


    public void error(String s, Object... objects)
    {
        Object[] params = stackTrackSafeParams(objects);
        this.logger.error(s, params);
    }


    public void error(String s, Throwable throwable)
    {
        Object[] params = {throwable};
        error(s, params);
    }


    public boolean isErrorEnabled(Marker marker)
    {
        return this.logger.isErrorEnabled(marker);
    }


    public void error(Marker marker, String s)
    {
        this.logger.error(marker, s);
    }


    public void error(Marker marker, String s, Object o)
    {
        this.logger.error(marker, s, o);
    }


    public void error(Marker marker, String s, Object o, Object o1)
    {
        Object[] params = {o, o1};
        error(marker, s, params);
    }


    public void error(Marker marker, String s, Object... objects)
    {
        Object[] params = stackTrackSafeParams(objects);
        this.logger.error(marker, s, params);
    }


    public void error(Marker marker, String s, Throwable throwable)
    {
        Object[] params = {throwable};
        error(marker, s, params);
    }


    private Object[] stackTrackSafeParams(Object[] objects)
    {
        return (isDebugEnabled() || !isStackTraceLogged(objects)) ?
                        objects :
                        removeStackTraceFromBeingLogged(objects);
    }


    private Object[] removeStackTraceFromBeingLogged(Object[] objects)
    {
        Object[] objectsWithoutLastElement = Arrays.copyOf(objects, objects.length - 1);
        return stackTrackSafeParams(objectsWithoutLastElement);
    }


    private static boolean isStackTraceLogged(Object[] params)
    {
        return (params.length > 0 && params[params.length - 1] instanceof Throwable);
    }
}

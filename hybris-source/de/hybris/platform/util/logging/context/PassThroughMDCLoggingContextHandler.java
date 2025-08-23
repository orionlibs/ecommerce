package de.hybris.platform.util.logging.context;

import java.io.Closeable;
import java.util.Map;
import org.slf4j.MDC;

public class PassThroughMDCLoggingContextHandler implements LoggingContextHandler
{
    public void put(String key, String val)
    {
        MDC.put(key, val);
    }


    public Closeable putCloseable(String key, String val)
    {
        return MDC.putCloseable(key, val);
    }


    public String get(String key)
    {
        return MDC.get(key);
    }


    public void remove(String key)
    {
        MDC.remove(key);
    }


    public void clear()
    {
        MDC.clear();
    }


    public Map<String, String> getCopyOfContextMap()
    {
        return MDC.getCopyOfContextMap();
    }


    public void setContextMap(Map<String, String> contextMap)
    {
        MDC.setContextMap(contextMap);
    }
}

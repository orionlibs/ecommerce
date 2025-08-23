package de.hybris.platform.util.logging.context;

import java.io.Closeable;
import java.util.Map;

public class NOPLoggingContextHandler implements LoggingContextHandler
{
    public void put(String key, String val)
    {
    }


    public Closeable putCloseable(String key, String val)
    {
        return null;
    }


    public String get(String key)
    {
        return null;
    }


    public void remove(String key)
    {
    }


    public void clear()
    {
    }


    public Map<String, String> getCopyOfContextMap()
    {
        return null;
    }


    public void setContextMap(Map<String, String> contextMap)
    {
    }
}

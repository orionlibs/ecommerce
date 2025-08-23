package de.hybris.platform.util.logging.context;

import java.io.Closeable;
import java.util.Map;

public interface LoggingContextHandler
{
    void put(String paramString1, String paramString2);


    Closeable putCloseable(String paramString1, String paramString2);


    String get(String paramString);


    void remove(String paramString);


    void clear();


    Map<String, String> getCopyOfContextMap();


    void setContextMap(Map<String, String> paramMap);
}

package de.hybris.platform.util.logging.log4j2;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;

class HybrisClassLoaderContextSelector extends ClassLoaderContextSelector
{
    protected LoggerContext createContext(String name, URI configLocation)
    {
        return (LoggerContext)new HybrisLoggerContext(name, configLocation);
    }
}

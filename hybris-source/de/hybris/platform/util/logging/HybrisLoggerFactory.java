package de.hybris.platform.util.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class HybrisLoggerFactory implements LoggerFactory
{
    public Logger makeNewLoggerInstance(String name)
    {
        return (Logger)new HybrisLogger(name);
    }
}

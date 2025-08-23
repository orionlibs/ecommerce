package de.hybris.platform.util.logging.log4j2;

import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.core.selector.ContextSelector;

public class HybrisLog4j2LoggerContextFactory extends Log4jContextFactory
{
    public HybrisLog4j2LoggerContextFactory()
    {
        super((ContextSelector)new HybrisClassLoaderContextSelector());
    }
}

package de.hybris.platform.util.logging.log4j2;

import java.net.URI;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.message.MessageFactory;

public class HybrisLoggerContext extends LoggerContext
{
    public HybrisLoggerContext(String name, URI configLocation)
    {
        super(name, null, configLocation);
    }


    protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory)
    {
        return (Logger)new HybrisLog4j2Logger(ctx, name, messageFactory);
    }


    public void shutdown()
    {
        Configurator.shutdown(this);
    }
}

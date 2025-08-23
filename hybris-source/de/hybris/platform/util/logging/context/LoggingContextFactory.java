package de.hybris.platform.util.logging.context;

import de.hybris.platform.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingContextFactory
{
    static final String CONFIG_LOGGINGCONTEXT_HANDLER_CLASS = "loggingContext.handler.class";
    private static final Logger LOG = LoggerFactory.getLogger(LoggingContextFactory.class);
    private static final PassThroughMDCLoggingContextHandler DEFAULT_FALLBACK_LOGGINGCONTEXT_HANDLER = new PassThroughMDCLoggingContextHandler();
    private static volatile LoggingContextFactoryState factoryState = LoggingContextFactoryState.UNINITIALIZED;
    private static volatile LoggingContextHandler mdcHandler;


    public static LoggingContextHandler getLoggingContextHandler()
    {
        if(factoryState.equals(LoggingContextFactoryState.UNINITIALIZED))
        {
            synchronized(LoggingContextFactory.class)
            {
                if(factoryState.equals(LoggingContextFactoryState.UNINITIALIZED))
                {
                    performInitialization();
                }
            }
        }
        switch(null.$SwitchMap$de$hybris$platform$util$logging$context$LoggingContextFactory$LoggingContextFactoryState[factoryState.ordinal()])
        {
            case 1:
                return mdcHandler;
        }
        return (LoggingContextHandler)DEFAULT_FALLBACK_LOGGINGCONTEXT_HANDLER;
    }


    static void reset()
    {
        factoryState = LoggingContextFactoryState.UNINITIALIZED;
    }


    static LoggingContextHandler getDefaultLoggingContextHandler()
    {
        return (LoggingContextHandler)DEFAULT_FALLBACK_LOGGINGCONTEXT_HANDLER;
    }


    private static void performInitialization()
    {
        String mdcHandlerClass = Utilities.getConfig().getString("loggingContext.handler.class", "");
        if(mdcHandlerClass == null || mdcHandlerClass.isEmpty())
        {
            factoryState = LoggingContextFactoryState.FALLBACK_INITIALIZATION;
            return;
        }
        try
        {
            Class<?> clazz = Class.forName(mdcHandlerClass);
            mdcHandler = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            factoryState = LoggingContextFactoryState.SUCCESSFUL_INITIALIZATION;
        }
        catch(NoSuchMethodException | java.lang.reflect.InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            LOG.warn("Could not load class {}. Fallback to {}", new Object[] {mdcHandlerClass, DEFAULT_FALLBACK_LOGGINGCONTEXT_HANDLER
                            .getClass().getName(), ex});
            factoryState = LoggingContextFactoryState.FALLBACK_INITIALIZATION;
        }
    }
}

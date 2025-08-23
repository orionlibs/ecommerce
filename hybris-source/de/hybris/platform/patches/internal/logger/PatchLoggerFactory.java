package de.hybris.platform.patches.internal.logger;

import de.hybris.platform.patches.internal.logger.impl.PatchLoggerImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PatchLoggerFactory
{
    private static final Map<Class, PatchLogger> LOGGERS = (Map)new ConcurrentHashMap<>();


    public static PatchLogger getLogger(Class clazz)
    {
        if(LOGGERS.containsKey(clazz))
        {
            return LOGGERS.get(clazz);
        }
        PatchLoggerImpl patchLoggerImpl = new PatchLoggerImpl(clazz);
        LOGGERS.put(clazz, patchLoggerImpl);
        return (PatchLogger)patchLoggerImpl;
    }
}

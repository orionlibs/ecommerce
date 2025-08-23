package de.hybris.platform.util.persistence;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;

public class PersistenceUtils
{
    public static final String PERSISTENCE_LEGACY_MODE = "persistence.legacy.mode";
    private static final String SESSION_SERVICE = "sessionService";


    public static <T> T doWithSLDPersistence(Supplier<T> supplier)
    {
        return doWithChangedPersistenceLegacyMode(false, supplier);
    }


    public static <T> T doWithLegacyPersistence(Supplier<T> supplier)
    {
        return doWithChangedPersistenceLegacyMode(true, supplier);
    }


    public static <T> T doWithChangedPersistenceLegacyMode(boolean isLegacyMode, Supplier<T> supplier)
    {
        Objects.requireNonNull(supplier, "supplier mustn't be null");
        ImmutableMap<String, Object> params = ImmutableMap.of("persistence.legacy.mode", Boolean.valueOf(isLegacyMode));
        return (T)getSessionService().executeInLocalViewWithParams((Map)params, (SessionExecutionBody)new Object(supplier));
    }


    public static boolean isPersistenceLegacyModeEnabled()
    {
        Boolean isEnabledInSession = isPersistenceLagacyModeEnabledInSession();
        if(isEnabledInSession != null)
        {
            return isEnabledInSession.booleanValue();
        }
        return isPersistenceLagacyModeEnabledInConfig();
    }


    public static boolean isPersistenceLagacyModeEnabledInConfig()
    {
        return Config.getBoolean("persistence.legacy.mode", false);
    }


    private static Boolean isPersistenceLagacyModeEnabledInSession()
    {
        if(!Registry.hasCurrentTenant())
        {
            return null;
        }
        SessionService sessionService = getSessionService();
        Object valueFromSession = sessionService.getAttribute("persistence.legacy.mode");
        if(valueFromSession == null)
        {
            return null;
        }
        if(!(valueFromSession instanceof Boolean))
        {
            throw new SystemException("Value of attribute 'persistence.legacy.mode' mus be instance of Boolean");
        }
        return (Boolean)valueFromSession;
    }


    private static SessionService getSessionService()
    {
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        return (SessionService)ctx.getBean("sessionService", SessionService.class);
    }
}

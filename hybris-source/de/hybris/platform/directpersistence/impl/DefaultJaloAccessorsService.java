package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.DefaultSLDUnsafeTypesProvider;
import de.hybris.platform.directpersistence.JaloAccessorsService;
import de.hybris.platform.directpersistence.SLDUnsafeTypesProvider;
import de.hybris.platform.directpersistence.SLDUnsafeTypesProviderBuilder;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultJaloAccessorsService implements JaloAccessorsService, InitializingBean, DisposableBean, ConfigIntf.ConfigChangeListener
{
    public static final String CFG_IGNORE_MARKED_UNSAFE = "direct.persistence.ignore.marked.unsafe";
    public static final String CFG_ALL_SAFE_4_READING = "direct.persistence.ignore.all.unsafe.reads";
    public static final String CFG_ALL_SAFE_4_WRITING = "direct.persistence.ignore.all.unsafe.writes";
    public static final String CFG_IGNORE_UNSAFE_TYPE_PREFIX = "direct.persistence.ignore.unsafe.type.";
    private SLDUnsafeTypesProviderBuilder sldUnsafeTypesProviderBuilder;
    private SessionService sessionService;
    private volatile SLDUnsafeTypesProvider sldUnsafeTypesProvider;
    private volatile boolean ignoreMarkedMethods = false;
    private volatile boolean allSafeForReading = false;
    private volatile boolean allSafeForwriting = false;
    private final Map<String, Boolean> ignoreUnsafeTypeCache = new ConcurrentHashMap<>();
    private ConfigIntf cfg;


    public void afterPropertiesSet() throws Exception
    {
        this.cfg = Registry.getCurrentTenantNoFallback().getConfig();
        this.cfg.registerConfigChangeListener(this);
        updateIgnoreMarkedSettings();
    }


    public void configChanged(String key, String newValue)
    {
        switch(key)
        {
            case "direct.persistence.ignore.marked.unsafe":
            case "direct.persistence.ignore.all.unsafe.reads":
            case "direct.persistence.ignore.all.unsafe.writes":
                updateIgnoreMarkedSettings();
                return;
        }
        if(key.startsWith("direct.persistence.ignore.unsafe.type."))
        {
            updateIgnoreMarkedSettings();
        }
    }


    public void destroy() throws Exception
    {
        if(this.cfg != null)
        {
            this.cfg.unregisterConfigChangeListener(this);
        }
    }


    protected void updateIgnoreMarkedSettings()
    {
        this.ignoreMarkedMethods = this.cfg.getBoolean("direct.persistence.ignore.marked.unsafe", false);
        this.allSafeForReading = this.cfg.getBoolean("direct.persistence.ignore.all.unsafe.reads", false);
        this.allSafeForwriting = this.cfg.getBoolean("direct.persistence.ignore.all.unsafe.writes", false);
        this.ignoreUnsafeTypeCache.clear();
    }


    protected boolean isTypeConfiguredAsSafe(String typeCode)
    {
        Boolean cachedResult = this.ignoreUnsafeTypeCache.get(typeCode);
        if(cachedResult == null)
        {
            cachedResult = Boolean.valueOf(this.cfg.getBoolean("direct.persistence.ignore.unsafe.type." + typeCode, false));
            this.ignoreUnsafeTypeCache.put(typeCode, cachedResult);
        }
        return cachedResult.booleanValue();
    }


    public boolean isSLDSafe(String typeCode)
    {
        if(isLegacyModeOn())
        {
            return false;
        }
        return ((this.allSafeForReading && this.allSafeForwriting) || isTypeConfiguredAsSafe(typeCode) || provider().isSLDSafe(typeCode, this.ignoreMarkedMethods));
    }


    public boolean isSLDSafeForRead(String typeCode)
    {
        if(isLegacyModeOn())
        {
            return false;
        }
        return (this.allSafeForReading || isTypeConfiguredAsSafe(typeCode) || provider().isSLDSafe(typeCode, true, this.ignoreMarkedMethods));
    }


    public boolean isSLDSafeForWrite(String typeCode)
    {
        if(isLegacyModeOn())
        {
            return false;
        }
        return (this.allSafeForwriting || isTypeConfiguredAsSafe(typeCode) || provider().isSLDSafe(typeCode, false, this.ignoreMarkedMethods));
    }


    private boolean isLegacyModeOn()
    {
        Object sessionPersistenceMode = this.sessionService.getAttribute("persistence.legacy.mode");
        if(sessionPersistenceMode instanceof Boolean)
        {
            return ((Boolean)sessionPersistenceMode).booleanValue();
        }
        if(sessionPersistenceMode instanceof String)
        {
            return Boolean.valueOf((String)sessionPersistenceMode).booleanValue();
        }
        return Registry.getCurrentTenant().getConfig().getBoolean("persistence.legacy.mode", false);
    }


    private SLDUnsafeTypesProvider provider()
    {
        if(this.sldUnsafeTypesProvider == null)
        {
            synchronized(DefaultSLDUnsafeTypesProvider.class)
            {
                if(this.sldUnsafeTypesProvider == null)
                {
                    this.sldUnsafeTypesProvider = this.sldUnsafeTypesProviderBuilder.build(null);
                }
            }
        }
        return this.sldUnsafeTypesProvider;
    }


    @Required
    public void setSLDUnsafeTypesProviderBuilder(SLDUnsafeTypesProviderBuilder sldUnsafeTypesProviderBuilder)
    {
        this.sldUnsafeTypesProviderBuilder = sldUnsafeTypesProviderBuilder;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}

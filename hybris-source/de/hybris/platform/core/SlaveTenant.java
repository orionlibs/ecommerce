package de.hybris.platform.core;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.impl.CacheFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.extension.ItemLifecycleListener;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.config.FallbackConfig;
import de.hybris.platform.util.config.HybrisConfig;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import org.springframework.context.support.GenericApplicationContext;

public class SlaveTenant extends AbstractTenant
{
    private static final Logger LOG = Logger.getLogger(Registry.class.getName());
    public static final String JUNIT_TENANT_ID = "junit";
    public static final String ITEM_CREATION_LIFECYCLE_LISTENER = "ItemCreationLifecycleListener";
    private final ConfigIntf ownConfig;
    private final FallbackConfig config;
    private final TimeZone timeZone;
    private final Locale locale;
    private final List<String> extNames;
    private volatile boolean connectionValid = false;
    private boolean active = true;


    public SlaveTenant(String systemName, Properties props)
    {
        super(systemName);
        this
                        .ownConfig = (ConfigIntf)new HybrisConfig(readConfigParamsAsMap(props), Registry.isStandaloneMode(), Registry.getPreferredClusterID());
        this.config = new FallbackConfig(this.ownConfig, getMasterTenant().getConfig());
        this.timeZone = computeTimeZone(this.ownConfig);
        this.locale = computeTenantLocale(this.ownConfig);
        this
                        .extNames = Collections.unmodifiableList(extractExtNames(this.ownConfig.getParameter(Config.SystemSpecificParams.EXTENSIONS)));
    }


    protected MasterTenant getMasterTenant()
    {
        return Registry.getMasterTenant();
    }


    protected List<AbstractTenant.DataSourceHolder> createAlternativeDataSources(DataSourceFactory factory, ConfigIntf cfg, Collection<HybrisDataSource> createdForRollback)
    {
        return super.createAlternativeDataSources(factory, this.ownConfig, createdForRollback);
    }


    protected void relaseAdministrationLockIfNeeded(HybrisDataSource masterDataSource)
    {
    }


    protected List<String> extractExtNames(String cfg)
    {
        if(cfg != null)
        {
            Collection<String> installed = Utilities.getInstalledExtensionNames((Tenant)getMasterTenant());
            Iterable<String> allFromConfig = Splitter.on(";").omitEmptyStrings().trimResults().split(cfg);
            List<String> allNames = Lists.newArrayList(Iterables.filter(allFromConfig, (Predicate)new Object(this, installed)));
            if(!allNames.isEmpty() && !allNames.contains("core"))
            {
                return (List<String>)ImmutableList.builder().add("core").addAll(allNames).build();
            }
            return (List<String>)ImmutableList.copyOf(allNames);
        }
        return Collections.EMPTY_LIST;
    }


    protected void setExtNames(List<String> names_unmod)
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    private Properties readConfigParamsAsMap(Map<String, Object> params)
    {
        Properties ret = new Properties();
        for(Map.Entry<String, ?> e : params.entrySet())
        {
            if(e.getValue() instanceof String)
            {
                ret.setProperty(e.getKey(), (String)e.getValue());
                if(LOG.isDebugEnabled())
                {
                    if(!((String)e.getKey()).matches("slave\\.datasource\\.[A-Z]\\.db\\.password"))
                    {
                        LOG.debug("read tenant system param " + (String)e.getKey() + "='" + e.getValue() + "'");
                    }
                }
                continue;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("cannot read tenant system param " + (String)e.getKey() + "='" + e.getValue() + "' since it is no string");
            }
        }
        ret.setProperty(Config.Params.CLUSTER_ID, Integer.toString(
                        getClusterID()));
        return ret;
    }


    protected Cache createCache()
    {
        if(getConfig().getBoolean("cache.shared", false))
        {
            return (new CacheFactory()).createCacheInstance((Tenant)this, (Tenant)Registry.getMasterTenant());
        }
        return super.createCache();
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public synchronized void unInitialize()
    {
        Tenant prev = null;
        if(Registry.hasCurrentTenant() && (prev = Registry.getCurrentTenantNoFallback()) != this)
        {
            Registry.unsetCurrentTenant();
        }
        Registry.setCurrentTenant((Tenant)this);
        try
        {
            SystemEJB.getInstance().setInitializedFlag(false);
        }
        finally
        {
            if(prev != null)
            {
                if(prev != this)
                {
                    Registry.unsetCurrentTenant();
                    if(prev instanceof SlaveTenant)
                    {
                        Registry.setCurrentTenant(prev);
                    }
                    else
                    {
                        Registry.activateMasterTenantAndFailIfAlreadySet();
                    }
                }
            }
            else
            {
                Registry.unsetCurrentTenant();
            }
        }
    }


    public boolean isInitialized()
    {
        try
        {
            return Utilities.isSystemInitialized(getDataSource());
        }
        catch(IllegalStateException e)
        {
            LOG.debug(e);
            return false;
        }
    }


    public synchronized boolean changeSystemSpecificProps(String key, String value)
    {
        return changeSystemSpecificProps(Collections.singletonMap(key, value));
    }


    public synchronized boolean changeSystemSpecificProps(Map<String, String> params)
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    public boolean isActive()
    {
        return (!cannotAccess() && this.active);
    }


    public boolean isValid()
    {
        return this.connectionValid;
    }


    void shutDown(AbstractTenant.ShutDownMode mode)
    {
        shutDown();
    }


    public void shutDown()
    {
        this.active = false;
        this.connectionValid = false;
    }


    PK getMetaInfoPK()
    {
        throw new UnsupportedOperationException();
    }


    public void startUp()
    {
        this.active = !"false".equalsIgnoreCase(this.ownConfig.getParameter("system.active"));
        this.connectionValid = Utilities.isDBConnectionValid(getDatabaseURL(), getDatabaseUser(), getDatabasePassword(),
                        getDatabaseDriver(), getDatabaseFromJNDI());
    }


    public synchronized void setDatabaseSettings(String url, String user, String password, String driver, String tableprefix, String fromJNDI)
    {
        throw new UnsupportedOperationException("This operation is not allowed, please provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    @Deprecated(since = "5.0", forRemoval = true)
    protected void restartIfActive()
    {
        if(getState() == AbstractTenant.State.STARTED)
        {
            Tenant current = Registry.getCurrentTenantNoFallback();
            doShutDown();
            if(current != null && current == this)
            {
                try
                {
                    doStartUp();
                }
                catch(ConsistencyCheckException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
    }


    public String getDatabaseURL()
    {
        return this.config.getParameter(Config.SystemSpecificParams.DB_URL);
    }


    public String getDatabaseUser()
    {
        return this.config.getParameter(Config.SystemSpecificParams.DB_USERNAME);
    }


    public String getDatabaseDriver()
    {
        return this.config.getParameter(Config.SystemSpecificParams.DB_DRIVER);
    }


    public String getDatabasePassword()
    {
        return this.config.getParameter(Config.SystemSpecificParams.DB_PASSWORD);
    }


    public String getDatabaseTablePrefix()
    {
        return this.config.getParameter(Config.SystemSpecificParams.DB_TABLEPREFIX);
    }


    public String getDatabaseFromJNDI()
    {
        return this.config.getString(Config.SystemSpecificParams.DB_POOL_FROMJNDI, null);
    }


    public ConfigIntf getOwnConfig()
    {
        return this.ownConfig;
    }


    public List<String> getTenantSpecificExtensionNames()
    {
        Preconditions.checkArgument(!this.extNames.isEmpty(), "Empty tenant specific extension list for " + this + ", indicates some tenant configuration issue.");
        if(LOG.isDebugEnabled())
        {
            LOG.debug(Joiner.on(",").join("Extension names :", this, new Object[] {this.extNames}));
        }
        return this.extNames;
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public void setTenantSpecificExtensionNames(List<String> names)
    {
        setExtNames(names);
    }


    public Locale getTenantSpecificLocale()
    {
        return this.locale;
    }


    private final Locale computeTenantLocale(ConfigIntf ownConfig)
    {
        Locale ret = null;
        String myLocaleStr = ownConfig.getString(Config.SystemSpecificParams.LOCALE, null);
        if(myLocaleStr != null && myLocaleStr.length() > 0)
        {
            String[] tokens = myLocaleStr.split("_");
            if(tokens.length > 0 && tokens[0].length() > 0)
            {
                String language = tokens[0];
                String country = (tokens.length > 1 && tokens[1].length() > 0) ? tokens[1] : null;
                String variant = (country != null && tokens.length > 2 && tokens[2].length() > 0) ? tokens[2] : null;
                Locale tmp = (variant != null) ? new Locale(language, country, variant) : ((country != null) ? new Locale(language, country) : new Locale(language));
                if(!(new HashSet(Arrays.asList((Object[])Locale.getAvailableLocales()))).contains(tmp))
                {
                    LOG.error("illegal tenant specific locale '" + myLocaleStr + "' for tenant " + getTenantID() + " - not supported by java virtual machine");
                }
                else
                {
                    ret = tmp;
                }
            }
        }
        if(ret == null)
        {
            ret = Locale.getDefault();
        }
        return ret;
    }


    public TimeZone getTenantSpecificTimeZone()
    {
        return this.timeZone;
    }


    private final TimeZone computeTimeZone(ConfigIntf ownConfig)
    {
        TimeZone ret = null;
        String myTZStr = ownConfig.getString(Config.SystemSpecificParams.TIME_ZONE, null);
        if(myTZStr != null && myTZStr.length() > 0)
        {
            ret = TimeZone.getTimeZone(myTZStr);
        }
        if(ret == null)
        {
            ret = TimeZone.getDefault();
        }
        return ret;
    }


    public ConfigIntf getConfig()
    {
        return (ConfigIntf)this.config;
    }


    public String toString()
    {
        return "<<" + getTenantID() + ">>[" + hashCode() + "]";
    }


    public List<ItemLifecycleListener> getAllItemLifecycleListeners()
    {
        GenericApplicationContext genericApplicationContext = getApplicationContext();
        List<ItemLifecycleListener> itemLifecycleListeners = new LinkedList<>();
        if(genericApplicationContext != null && genericApplicationContext.containsBean("ItemCreationLifecycleListener") && "junit".equals(getTenantID()))
        {
            ItemLifecycleListener itemCreationLifecycleListener = (ItemLifecycleListener)genericApplicationContext.getBean("ItemCreationLifecycleListener", ItemLifecycleListener.class);
            itemLifecycleListeners.add(itemCreationLifecycleListener);
        }
        itemLifecycleListeners.addAll(getJaloConnection().getExtensionManager().getExtensions());
        return itemLifecycleListeners;
    }
}

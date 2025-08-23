package de.hybris.platform.regioncache.region.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.regioncache.region.CleanUpQueryCacheRegionStrategy;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.threadpool.PoolableThread;
import java.util.Objects;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class DefaultCleanUpQueryCacheRegionService implements InitializingBean, DisposableBean
{
    public static final String CFG_CACHE_CLEAN_INTERVAL = "queryRegionCache.clean.interval";
    public static final String CFG_CACHE_CLEAN_ENABLED = "queryRegionCache.clean.enabled";
    public static final int DEFAULT_CACHE_CLEAN_INTERVAL_SECONDS = 120;
    public static final boolean DEFAULT_CACHE_CLEAN_ENABLED = true;
    private int intervalSeconds;
    private volatile CleanUpQueryCacheRegionScheduledRunnable cleanUpQueryCacheRegionRunnable = null;
    private volatile boolean enabled;
    private Tenant myTenant;
    private CleanUpQueryCacheRegionStrategy cleanUpQueryCacheRegionStrategy;


    public DefaultCleanUpQueryCacheRegionService(CleanUpQueryCacheRegionStrategy cleanUpQueryCacheRegionStrategy)
    {
        Objects.requireNonNull(cleanUpQueryCacheRegionStrategy, "cleanUpQueryCacheRegionStrategy should be not null");
        this.cleanUpQueryCacheRegionStrategy = cleanUpQueryCacheRegionStrategy;
    }


    public void clearCache()
    {
        this.cleanUpQueryCacheRegionStrategy.cleanUp();
    }


    public void afterPropertiesSet() throws Exception
    {
        this.myTenant = Registry.getCurrentTenantNoFallback();
        setupFromConfig();
        setupWorkerViaTenantListener();
    }


    private void setupFromConfig()
    {
        ConfigIntf cfg = this.myTenant.getConfig();
        setInterval(cfg.getInt("queryRegionCache.clean.interval", 120));
        setEnabled(cfg.getBoolean("queryRegionCache.clean.enabled", true));
    }


    private void setInterval(int interval)
    {
        this.intervalSeconds = interval;
    }


    private void setEnabled(boolean enableSchedulerGlobal)
    {
        this.enabled = enableSchedulerGlobal;
    }


    private void setupWorkerViaTenantListener()
    {
        Registry.registerTenantListener((TenantListener)new Object(this));
    }


    private void applyWorkerSettings()
    {
        if(isEnabled())
        {
            startWorkerIfNotRunning();
        }
        else
        {
            stopWorkerIfRunning();
        }
    }


    private void startWorkerIfNotRunning()
    {
        if(this.cleanUpQueryCacheRegionRunnable == null)
        {
            synchronized(this)
            {
                if(this.cleanUpQueryCacheRegionRunnable == null)
                {
                    this.cleanUpQueryCacheRegionRunnable = new CleanUpQueryCacheRegionScheduledRunnable(this);
                    PoolableThread thread = this.myTenant.getThreadPool().borrowThread();
                    thread.execute((Runnable)this.cleanUpQueryCacheRegionRunnable);
                }
            }
        }
    }


    private void stopWorkerIfRunning()
    {
        if(this.cleanUpQueryCacheRegionRunnable != null)
        {
            synchronized(this)
            {
                if(this.cleanUpQueryCacheRegionRunnable != null)
                {
                    this.cleanUpQueryCacheRegionRunnable.cancel();
                    this.cleanUpQueryCacheRegionRunnable = null;
                }
            }
        }
    }


    public boolean isEnabled()
    {
        return this.enabled;
    }


    public void destroy()
    {
        stopWorkerIfRunning();
    }
}

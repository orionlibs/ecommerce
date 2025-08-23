package de.hybris.platform.util.config;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.threadpool.PoolableThread;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeConfigUpdateListener implements TenantListener
{
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeConfigUpdateListener.class);
    static final String RUNTIME_CONFIG_LOADER_CLASS = "runtime.config.loader.class";
    private static final String RUNTIME_CONFIG_REFRESH_TIME_SECONDS = "runtime.config.refresh.time.seconds";
    private volatile RuntimeConfigUpdateRunnable runnable;
    private final Tenant currentTenant;
    private final ConfigIntf config;
    private final RuntimeConfigLoader loader;


    public RuntimeConfigUpdateListener(Tenant currentTenant)
    {
        this.currentTenant = currentTenant;
        this.config = currentTenant.getConfig();
        this.loader = getConfigLoader(this.config);
    }


    private Tenant getCurrentTenant()
    {
        return this.currentTenant;
    }


    static RuntimeConfigLoader getConfigLoader(ConfigIntf config)
    {
        String loaderClassName = config.getParameter("runtime.config.loader.class");
        if(StringUtils.isBlank(loaderClassName))
        {
            LOG.debug("RuntimeConfigLoaderClass is not configured. RuntimeConfig worker will not start.");
            return null;
        }
        try
        {
            Class<?> clazz = Class.forName(loaderClassName);
            Preconditions.checkArgument(RuntimeConfigLoader.class.isAssignableFrom(clazz), "Configured class " + loaderClassName + " does not implement RuntimeConfigLoader interface");
            return (RuntimeConfigLoader)clazz.newInstance();
        }
        catch(ClassNotFoundException | IllegalAccessException | InstantiationException e)
        {
            throw new SystemException("Cannot instantiate RuntimeConfigLoader implementation class [reason: {}]. Runtime config update worker will not start.", e);
        }
    }


    public void afterTenantStartUp(Tenant tenant)
    {
        if(shouldStartDynamicConfigWorker(tenant))
        {
            startWorker();
        }
    }


    private boolean shouldStartDynamicConfigWorker(Tenant tenant)
    {
        return (this.loader != null && getCurrentTenant().equals(tenant) && getCurrentTenant().getJaloConnection().isSystemInitialized() &&
                        !RedeployUtilities.isShutdownInProgress());
    }


    private void startWorker()
    {
        if(this.runnable == null)
        {
            synchronized(this)
            {
                if(this.runnable == null)
                {
                    this.runnable = new RuntimeConfigUpdateRunnable(this, this.loader);
                    PoolableThread thread = getCurrentTenant().getThreadPool().borrowThread();
                    thread.execute((Runnable)this.runnable);
                }
            }
        }
    }


    public void beforeTenantShutDown(Tenant tenant)
    {
        if(getCurrentTenant().equals(tenant))
        {
            stopWroker();
        }
    }


    private void stopWroker()
    {
        if(this.runnable != null)
        {
            synchronized(this)
            {
                if(this.runnable != null)
                {
                    this.runnable.cancel();
                    this.runnable = null;
                }
            }
        }
    }


    public void afterSetActivateSession(Tenant tenant)
    {
    }


    public void beforeUnsetActivateSession(Tenant tenant)
    {
    }
}

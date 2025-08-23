package de.hybris.platform.core;

import de.hybris.platform.jalo.JaloSession;
import java.util.concurrent.ThreadFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

public class TenantAwareThreadFactory implements ThreadFactory, InitializingBean, BeanNameAware
{
    private final Tenant tenant;
    private Tenant previousTenant = null;
    private final JaloSession session;
    private final WaitForTenantStartup threadFactoryTenantListener;
    private String beanName = "";


    public TenantAwareThreadFactory(Tenant tenant, JaloSession session)
    {
        this.tenant = tenant;
        this.session = session;
        this.threadFactoryTenantListener = new WaitForTenantStartup(tenant);
    }


    public TenantAwareThreadFactory(Tenant tenant)
    {
        this(tenant, null);
    }


    private void innerPrepareThread()
    {
        try
        {
            beforePrepareThread();
            this.previousTenant = Registry.getCurrentTenantNoFallback();
            Registry.setCurrentTenant(this.tenant);
            if(this.session == null)
            {
                JaloSession.getCurrentSession().activate();
            }
            else
            {
                this.session.activate();
            }
        }
        finally
        {
            afterPrepareThread();
        }
    }


    protected void beforePrepareThread()
    {
        this.threadFactoryTenantListener.lockExecutionIfNeeded();
    }


    protected void afterPrepareThread()
    {
    }


    private void innerUnprepareThread()
    {
        try
        {
            beforeUnprepareThread();
            JaloSession.deactivate();
        }
        finally
        {
            if(this.previousTenant != null)
            {
                Registry.setCurrentTenant(this.previousTenant);
            }
            else
            {
                Registry.unsetCurrentTenant();
            }
            afterUnprepareThread();
        }
    }


    protected void beforeUnprepareThread()
    {
    }


    protected void afterUnprepareThread()
    {
    }


    public Thread newThread(Runnable runnable)
    {
        return (Thread)new Object(this, runnable);
    }


    public void afterPropertiesSet() throws Exception
    {
        this.threadFactoryTenantListener.initLock(this.beanName);
        Registry.registerTenantListener((TenantListener)this.threadFactoryTenantListener);
    }


    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }
}

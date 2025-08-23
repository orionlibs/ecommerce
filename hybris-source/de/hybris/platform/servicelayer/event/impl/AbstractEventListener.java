package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.tenant.TenantService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

public abstract class AbstractEventListener<T extends AbstractEvent> implements ApplicationListener<T>, ApplicationContextAware, InitializingBean
{
    private static final Logger LOG = Logger.getLogger(AbstractEventListener.class.getName());
    private TenantService tenantService;
    private ClusterService clusterService;
    private ApplicationContext applicationContext;


    public final void onApplicationEvent(T event)
    {
        if(EventUtils.matchCluster((AbstractEvent)event, this.clusterService.getClusterIslandId(), this.clusterService.getClusterId(), this.clusterService
                        .getClusterGroups()))
        {
            EventScope scope = event.getScope();
            String tenantID = (scope == null) ? null : scope.getTenantId();
            if(tenantID == null || tenantID.equals(this.tenantService.getCurrentTenantId()))
            {
                onEvent(event);
            }
        }
    }


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public void afterPropertiesSet()
    {
        if(this.tenantService == null)
        {
            LOG.warn("Found missing tenantService dependency for bean " + this + ", please adjust spring configuration");
            ServicelayerUtils.enforceBeanScope(this.applicationContext, "tenantService", "singleton");
            setTenantService((TenantService)this.applicationContext.getBean("tenantService"));
        }
        if(this.clusterService == null)
        {
            LOG.warn("Found missing clusterService dependency for bean " + this + ", please adjust spring configuration");
            ServicelayerUtils.enforceBeanScope(this.applicationContext, "clusterService", "singleton");
            setClusterService((ClusterService)this.applicationContext.getBean("clusterService"));
        }
    }


    protected abstract void onEvent(T paramT);
}

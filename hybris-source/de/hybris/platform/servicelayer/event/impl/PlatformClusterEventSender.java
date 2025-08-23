package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.cluster.legacy.BinaryBroadcastListener;
import de.hybris.platform.cluster.legacy.LegacyBroadcastHandler;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.SerializationService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import java.util.concurrent.Executor;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class PlatformClusterEventSender extends SpringEventSender implements BinaryBroadcastListener
{
    private static final Logger LOG = Logger.getLogger(PlatformClusterEventSender.class.getName());
    private SerializationService serializationService;
    private volatile Executor executor = null;
    private Tenant tenant;


    public void sendEvent(AbstractEvent event)
    {
        boolean onlyToLocal = !(event instanceof ClusterAwareEvent);
        if(onlyToLocal)
        {
            if(this.executor != null)
            {
                this.executor.execute((Runnable)new AsyncEventSendingRunnable(this, Registry.getCurrentTenant(), event));
            }
            else
            {
                super.sendEvent(event);
            }
        }
        else
        {
            publishViaCluster((ClusterAwareEvent)event);
        }
    }


    private void publishViaCluster(ClusterAwareEvent event)
    {
        ClassLoader contextBefore = event.getClass().getClassLoader().equals(Thread.currentThread().getContextClassLoader()) ? null : Thread.currentThread().getContextClassLoader();
        try
        {
            if(contextBefore != null)
            {
                Thread.currentThread().setContextClassLoader(event.getClass().getClassLoader());
            }
            byte[] bytes = this.serializationService.serialize(event);
            LegacyBroadcastHandler.getInstance().sendBinaryCustomPacket(bytes);
        }
        finally
        {
            if(contextBefore != null)
            {
                Thread.currentThread().setContextClassLoader(contextBefore);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    @PostConstruct
    public void postConstruct()
    {
    }


    public void registerBinaryListenerHook()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("About to register " + this + " to " + LegacyBroadcastHandler.getInstance());
        }
        if(!RedeployUtilities.isShutdownInProgress())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Registered " + this + " to " + LegacyBroadcastHandler.getInstance());
            }
            LegacyBroadcastHandler.getInstance().registerBinaryListener(this);
        }
    }


    @PreDestroy
    public void preDestroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("About to unregister " + this + " to " + LegacyBroadcastHandler.getInstance());
        }
        if(!RedeployUtilities.isShutdownInProgress())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Unregistered " + this + " to " + LegacyBroadcastHandler.getInstance());
            }
            LegacyBroadcastHandler.getInstance().unregisterBinaryListener(this);
        }
    }


    public void processPacket(byte[] message)
    {
        try
        {
            AbstractEvent event = (AbstractEvent)this.serializationService.deserialize(message);
            if(isForOwnTenant(event))
            {
                event.setFromCluster(true);
                AbstractTenant targetTenant = (AbstractTenant)Registry.getTenantByID(event.getScope().getTenantId());
                if(targetTenant != null && targetTenant.getState() == AbstractTenant.State.STARTED)
                {
                    Registry.setCurrentTenant((Tenant)targetTenant);
                    super.sendEvent(event);
                }
            }
        }
        catch(Exception e)
        {
            boolean initialized;
            try
            {
                initialized = Utilities.isSystemInitialized(Registry.getMasterTenant().getDataSource());
            }
            catch(Exception e1)
            {
                LOG.debug("Error while checking if system is initialized", e1);
                initialized = false;
            }
            if((Registry.hasCurrentTenant() && Utilities.isSystemInitialized(
                            Registry.getCurrentTenant().getDataSource())) || initialized)
            {
                LOG.error("Error while receiving event: " + e.getMessage(), e);
            }
        }
        finally
        {
            JaloSession.deactivate();
            Registry.unsetCurrentTenant();
        }
    }


    protected boolean isForOwnTenant(AbstractEvent event)
    {
        return this.tenant.getTenantID().equals(event.getScope().getTenantId());
    }


    public void setExecutor(Executor executor)
    {
        this.executor = executor;
        if(executor != null)
        {
            LOG.info("Local events will be send asynchronous.");
        }
    }


    public Executor getExecutor()
    {
        return this.executor;
    }


    @Required
    public void setSerializationService(SerializationService serializationService)
    {
        this.serializationService = serializationService;
    }


    @Required
    public void setTenant(Tenant tenant)
    {
        this.tenant = tenant;
    }
}

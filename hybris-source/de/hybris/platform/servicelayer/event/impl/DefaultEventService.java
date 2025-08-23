package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.event.EventSender;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.TransactionAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.tx.Transaction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;

public class DefaultEventService extends AbstractService implements EventService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEventService.class);
    private TenantService tenantService;
    private ClusterService clusterService;
    private ApplicationEventMulticaster appEventMulticaster;
    private EventSender eventSender;
    private List<EventSender> additionalEventSenders;


    public void publishEvent(AbstractEvent event)
    {
        EventScope scope = event.getScope();
        if(scope == null)
        {
            scope = new EventScope();
            event.setScope(scope);
        }
        scope.setTenantId(this.tenantService.getCurrentTenantId());
        scope.setClusterId(this.clusterService.getClusterId());
        scope.setClusterIslandId(this.clusterService.getClusterIslandId());
        if(event instanceof TransactionAwareEvent && ((TransactionAwareEvent)event).publishOnCommitOnly())
        {
            Transaction.current().executeOnCommit((Transaction.TransactionAwareExecution)new Object(this, event));
        }
        else
        {
            sendEventUsingAllSenders(event);
        }
    }


    public boolean registerEventListener(ApplicationListener listener)
    {
        if(listener instanceof AbstractEventListener)
        {
            ((AbstractEventListener)listener).setTenantService(this.tenantService);
            ((AbstractEventListener)listener).setClusterService(this.clusterService);
        }
        this.appEventMulticaster.addApplicationListener(listener);
        return true;
    }


    public boolean unregisterEventListener(ApplicationListener listener)
    {
        this.appEventMulticaster.removeApplicationListener(listener);
        return true;
    }


    public Set<ApplicationListener> getEventListeners()
    {
        if(this.appEventMulticaster instanceof HybrisApplicationEventMulticaster)
        {
            HybrisApplicationEventMulticaster haem = (HybrisApplicationEventMulticaster)this.appEventMulticaster;
            return new HashSet<>(haem.getApplicationListeners());
        }
        return new HashSet<>();
    }


    private void sendEventUsingAllSenders(AbstractEvent event)
    {
        getAllEventSenders().forEach(sender -> {
            try
            {
                sender.sendEvent(event);
            }
            catch(Exception e)
            {
                LOG.error("Error occured during event sending. Sender: {}, Event: {}.", new Object[] {sender, event, e});
            }
        });
    }


    private Stream<EventSender> getAllEventSenders()
    {
        EventSender senderToReturn = this.eventSender;
        List<EventSender> sendersToReturn = this.additionalEventSenders;
        return Stream.concat((senderToReturn == null) ? Stream.<EventSender>empty() : Stream.<EventSender>of(senderToReturn),
                        (sendersToReturn == null) ? Stream.<EventSender>empty() : sendersToReturn.stream());
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


    @Required
    public void setApplicationEventMulticaster(ApplicationEventMulticaster appEventMulticaster)
    {
        this.appEventMulticaster = appEventMulticaster;
    }


    @Required
    public void setEventSender(EventSender eventSender)
    {
        this.eventSender = eventSender;
    }


    public void setAdditionalEventSenders(List<EventSender> additionalEventSenders)
    {
        this.additionalEventSenders = additionalEventSenders;
    }
}

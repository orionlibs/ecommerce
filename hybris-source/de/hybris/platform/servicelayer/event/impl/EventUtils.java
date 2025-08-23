package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(EventUtils.class);


    @Deprecated(since = "ages", forRemoval = true)
    public static boolean matchCluster(AbstractEvent event, long currentClusterIslandId, int currentClusterId)
    {
        return matchCluster(event, currentClusterIslandId, currentClusterId, Collections.emptySet());
    }


    public static boolean matchCluster(AbstractEvent event, long currentClusterIslandId, int currentClusterId, Collection<String> currentNodeGroups)
    {
        boolean assumeMatch;
        EventScope eventScope = event.getScope();
        if(!(event instanceof ClusterAwareEvent))
        {
            assumeMatch = true;
        }
        else if(eventScope == null)
        {
            assumeMatch = true;
        }
        else if(currentClusterIslandId == eventScope.getClusterIslandId())
        {
            PublishEventContext publishEventContext = PublishEventContext.newBuilder().withSourceNodeId(eventScope.getClusterId()).withTargetNodeId(currentClusterId).withTargetNodeGroups(currentNodeGroups).build();
            assumeMatch = ((ClusterAwareEvent)event).canPublish(publishEventContext);
        }
        else
        {
            assumeMatch = false;
        }
        LOG.debug("Match cluster check for Event: {} --  event scope: {} -- matching result: {}", new Object[] {event, eventScope, Boolean.valueOf(assumeMatch)});
        return assumeMatch;
    }
}

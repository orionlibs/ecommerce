package de.hybris.platform.ruleengine.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.Objects;

public class KieModuleSwappingEvent extends AbstractEvent implements ClusterAwareEvent
{
    private final String rulesModuleName;
    private final String deployedReleaseIdVersion;


    public KieModuleSwappingEvent(String rulesModuleName, String deployedReleaseIdVersion)
    {
        this.rulesModuleName = rulesModuleName;
        this.deployedReleaseIdVersion = deployedReleaseIdVersion;
    }


    public boolean canPublish(PublishEventContext publishEventContext)
    {
        Objects.requireNonNull(publishEventContext, "publishEventContext is required");
        return (publishEventContext.getSourceNodeId() != publishEventContext.getTargetNodeId());
    }


    public String getRulesModuleName()
    {
        return this.rulesModuleName;
    }


    public String getDeployedReleaseIdVersion()
    {
        return this.deployedReleaseIdVersion;
    }
}

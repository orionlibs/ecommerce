package de.hybris.platform.ruleengineservices.maintenance;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class RulesCompilationInProgressQueryEvent extends AbstractEvent implements ClusterAwareEvent
{
    private final String moduleName;


    public RulesCompilationInProgressQueryEvent(String moduleName)
    {
        this.moduleName = moduleName;
    }


    public String getModuleName()
    {
        return this.moduleName;
    }


    public boolean canPublish(PublishEventContext publishEventContext)
    {
        return true;
    }
}

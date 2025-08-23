package de.hybris.platform.processengine.definition;

import de.hybris.platform.processengine.model.ProcessTaskModel;

public interface SynchronusAwareNode
{
    boolean canBeTriggeredForTask(ProcessTaskModel paramProcessTaskModel);


    void triggerForTask(ProcessTaskModel paramProcessTaskModel);
}

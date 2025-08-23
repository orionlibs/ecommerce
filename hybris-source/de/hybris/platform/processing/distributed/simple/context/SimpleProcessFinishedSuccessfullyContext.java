package de.hybris.platform.processing.distributed.simple.context;

import de.hybris.platform.processing.model.DistributedProcessModel;

public class SimpleProcessFinishedSuccessfullyContext extends SimpleAbstractTurnContext
{
    public SimpleProcessFinishedSuccessfullyContext(DistributedProcessModel process)
    {
        super(process);
    }


    public boolean processFailed()
    {
        return false;
    }


    public boolean processSucceeded()
    {
        return true;
    }
}

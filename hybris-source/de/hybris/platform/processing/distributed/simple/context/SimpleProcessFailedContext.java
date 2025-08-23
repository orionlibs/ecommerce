package de.hybris.platform.processing.distributed.simple.context;

import de.hybris.platform.processing.model.DistributedProcessModel;

public class SimpleProcessFailedContext extends SimpleAbstractTurnContext
{
    public SimpleProcessFailedContext(DistributedProcessModel process)
    {
        super(process);
    }


    public boolean processFailed()
    {
        return true;
    }


    public boolean processSucceeded()
    {
        return false;
    }
}

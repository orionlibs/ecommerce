package de.hybris.platform.processengine.standard;

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;

public class NoAction extends AbstractProceduralAction
{
    public void executeAction(BusinessProcessModel process) throws RetryLaterException, Exception
    {
    }
}

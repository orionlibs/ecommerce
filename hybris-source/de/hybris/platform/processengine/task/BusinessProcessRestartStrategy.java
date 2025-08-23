package de.hybris.platform.processengine.task;

import de.hybris.platform.processengine.model.BusinessProcessModel;

public interface BusinessProcessRestartStrategy
{
    boolean makeARequestToRestartProcess(BusinessProcessModel paramBusinessProcessModel);
}

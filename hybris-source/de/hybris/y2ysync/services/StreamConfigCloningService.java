package de.hybris.y2ysync.services;

import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import java.util.Set;

public interface StreamConfigCloningService
{
    Y2YStreamConfigurationContainerModel cloneStreamContainer(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, String paramString);


    Set<Y2YStreamConfigurationModel> cloneStreamConfigurations(Y2YStreamConfigurationModel... paramVarArgs);
}

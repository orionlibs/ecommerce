package de.hybris.y2ysync.services;

import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;

public interface DataHubConfigService
{
    String createModelDefinitions(Y2YStreamConfigurationModel paramY2YStreamConfigurationModel, DataHubExtGenerationConfig paramDataHubExtGenerationConfig);


    String createModelDefinitions(Y2YStreamConfigurationModel paramY2YStreamConfigurationModel);


    String createModelDefinitions(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel);


    String createDataHubExtension(Y2YStreamConfigurationContainerModel paramY2YStreamConfigurationContainerModel, DataHubExtGenerationConfig paramDataHubExtGenerationConfig);
}

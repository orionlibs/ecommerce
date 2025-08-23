package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;
import java.util.List;

public interface ABTestService
{
    SimpleCMSComponentModel getRandomCmsComponent(ABTestCMSComponentContainerModel paramABTestCMSComponentContainerModel);


    List<SimpleCMSComponentModel> getRandomCMSComponents(ABTestCMSComponentContainerModel paramABTestCMSComponentContainerModel);
}

package de.hybris.platform.cms2.strategies.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.strategies.CMSComponentContainerStrategy;
import java.util.List;

public class DefaultCMSComponentContainerStrategy implements CMSComponentContainerStrategy
{
    public List<AbstractCMSComponentModel> getDisplayComponentsForContainer(AbstractCMSComponentContainerModel container)
    {
        return container.getSimpleCMSComponents();
    }
}

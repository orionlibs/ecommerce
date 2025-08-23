package de.hybris.platform.cms2.strategies.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cms2.strategies.CMSComponentContainerStrategy;
import java.util.List;

@Deprecated(since = "1811", forRemoval = true)
public class LegacyCMSComponentContainerStrategy implements CMSComponentContainerStrategy
{
    public List<AbstractCMSComponentModel> getDisplayComponentsForContainer(AbstractCMSComponentContainerModel container)
    {
        return container.getCurrentCMSComponents();
    }
}

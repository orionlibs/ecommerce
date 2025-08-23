package de.hybris.platform.cms2.strategies;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import java.util.List;

public interface CMSComponentContainerStrategy
{
    List<AbstractCMSComponentModel> getDisplayComponentsForContainer(AbstractCMSComponentContainerModel paramAbstractCMSComponentContainerModel);


    @Deprecated(since = "1811", forRemoval = true)
    default Class<? extends AbstractCMSComponentContainerModel> getContainerClass()
    {
        throw new UnsupportedOperationException();
    }
}

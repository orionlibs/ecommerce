package de.hybris.platform.cms2.registry;

import de.hybris.platform.cms2.strategies.CMSComponentContainerStrategy;

public interface CMSComponentContainerRegistry
{
    <T extends de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel> CMSComponentContainerStrategy getStrategy(T paramT);
}

package de.hybris.platform.persistence.links;

import de.hybris.platform.jalo.link.LinkOperationFactory;

public interface PluggableLinkOperationFactory extends LinkOperationFactory
{
    boolean isEnabled();
}

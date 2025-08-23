package de.hybris.platform.cms2.version.strategies;

import de.hybris.platform.core.PK;
import java.util.Set;

public interface CMSVersionCollectRetainableStrategy
{
    Set<PK> fetchRetainableVersions() throws Exception;
}

package de.hybris.platform.cms2.version.strategies;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import java.util.Collection;
import java.util.Optional;

public interface CMSVersionDeleteStrategy
{
    Optional<PerformResult> deleteVersions(Collection<PK> paramCollection) throws Exception;
}

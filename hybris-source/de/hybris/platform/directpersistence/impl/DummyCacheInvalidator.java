package de.hybris.platform.directpersistence.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.PersistResult;
import java.util.Collection;
import org.apache.log4j.Logger;

public class DummyCacheInvalidator implements CacheInvalidator
{
    private static final Logger LOG = Logger.getLogger(DummyCacheInvalidator.class);


    public void invalidate(Collection<PersistResult> results)
    {
        Preconditions.checkArgument((results != null), "at least one argument is required");
        for(PersistResult result : results)
        {
            LOG.debug("Invalidate PK " + result.getPk() + " for operation " + result.getOperation());
        }
    }
}

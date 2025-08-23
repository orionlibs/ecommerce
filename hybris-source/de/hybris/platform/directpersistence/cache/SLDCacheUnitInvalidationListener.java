package de.hybris.platform.directpersistence.cache;

import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.logging.Logs;
import org.apache.log4j.Logger;

public class SLDCacheUnitInvalidationListener implements InvalidationListener
{
    private static final Logger LOG = Logger.getLogger(SLDCacheUnitInvalidationListener.class);


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        PK pk = (PK)key[3];
        String typeCode = (String)key[2];
        Object[] _key = {"__SLD_CACHE__", typeCode, pk};
        Logs.debug(LOG, () -> "Invalidation attempt using cache key {__SLD_CACHE__, " + typeCode + ", " + pk + "}");
        target.invalidate(_key, invalidationType);
    }
}

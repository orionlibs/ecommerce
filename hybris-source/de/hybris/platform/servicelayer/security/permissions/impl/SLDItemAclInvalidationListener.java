package de.hybris.platform.servicelayer.security.permissions.impl;

import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;

public class SLDItemAclInvalidationListener implements InvalidationListener
{
    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        PK pk = (PK)key[3];
        target.invalidate(new Object[] {"__ACL_ITEM__", pk}, invalidationType);
        if(isPrincipal(pk))
        {
            target.invalidate(new Object[] {"__ACL_GLOBAL__", pk}, invalidationType);
        }
    }


    private boolean isPrincipal(PK pk)
    {
        return (pk.getTypeCode() == 4 || pk.getTypeCode() == 5);
    }
}

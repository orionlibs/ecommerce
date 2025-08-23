package de.hybris.platform.persistence.hjmp;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;

public class HJMPFindInvalidationListener implements InvalidationListener
{
    private final Object[] findKey;


    public HJMPFindInvalidationListener(String beanKey)
    {
        this
                        .findKey = new Object[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_FIND, beanKey.intern()};
    }


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        target.invalidate(this.findKey, invalidationType);
    }
}

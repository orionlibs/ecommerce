package de.hybris.platform.test;

import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.SimpleItemInvalidationListener;
import de.hybris.platform.core.PK;

class MyCachedPriceInvalidationListener extends SimpleItemInvalidationListener
{
    public void itemModified(InvalidationTarget cache, PK pk, boolean removed)
    {
        if(1 == pk.getTypeCode())
        {
            invalidate(cache, new Object[] {"myprice", pk}, removed);
        }
    }
}

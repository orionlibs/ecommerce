package de.hybris.platform.tx;

import de.hybris.platform.cache.AbstractCacheUnit;
import org.springframework.transaction.support.SmartTransactionObject;

public class DefaultTransaction extends Transaction implements SmartTransactionObject
{
    public boolean useCacheInternal(AbstractCacheUnit unit)
    {
        return true;
    }


    public void clearRollbackOnly()
    {
        super.clearRollbackOnly();
    }


    public final boolean isRollbackOnly()
    {
        return super.isRollbackOnly();
    }


    public void flush()
    {
        flushDelayedStore();
    }
}

package de.hybris.platform.cache;

public interface TransactionAwareCache
{
    boolean isForceExclusiveComputation(AbstractCacheUnit paramAbstractCacheUnit);


    void invalidate(AbstractCacheUnit paramAbstractCacheUnit, int paramInt);


    void removeUnit(AbstractCacheUnit paramAbstractCacheUnit);


    AbstractCacheUnit getOrAddUnit(AbstractCacheUnit paramAbstractCacheUnit);


    AbstractCacheUnit getUnit(AbstractCacheUnit paramAbstractCacheUnit);
}

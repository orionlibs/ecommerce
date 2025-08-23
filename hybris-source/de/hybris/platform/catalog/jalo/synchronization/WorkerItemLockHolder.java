package de.hybris.platform.catalog.jalo.synchronization;

public interface WorkerItemLockHolder
{
    void release(long paramLong, int paramInt);


    boolean lock(long paramLong1, int paramInt, long paramLong2);
}

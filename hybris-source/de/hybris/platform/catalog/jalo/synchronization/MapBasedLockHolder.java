package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.util.collections.fast.YLongToIntMap;

public class MapBasedLockHolder extends AbstractWorkerLockHolder
{
    private final YLongToIntMap pkWorkerMap;


    public MapBasedLockHolder(int maxWorkers)
    {
        this.pkWorkerMap = new YLongToIntMap(maxWorkers * 10);
    }


    protected void doRelease(int workerNumber, long pk)
    {
        int currentWorkerPos = this.pkWorkerMap.get(pk);
        if(currentWorkerPos == this.pkWorkerMap.getEmptyValue())
        {
            throw new IllegalStateException("pk " + pk + " is not locked at all - cannot release");
        }
        if(unescapeWorkerPosition(currentWorkerPos) != workerNumber)
        {
            throw new IllegalStateException("pk " + pk + " is locked by worker " + unescapeWorkerPosition(currentWorkerPos) + " - cannot release for " + workerNumber);
        }
        this.pkWorkerMap.remove(pk);
    }


    protected boolean doLock(long pk, int workerPosition)
    {
        int currentWorkerPos = this.pkWorkerMap.get(pk);
        if(currentWorkerPos == this.pkWorkerMap.getEmptyValue())
        {
            this.pkWorkerMap.put(pk, escapeWorkerPosition(workerPosition));
            return true;
        }
        return (currentWorkerPos == escapeWorkerPosition(workerPosition));
    }


    protected int escapeWorkerPosition(int workerPosition)
    {
        return workerPosition + 1;
    }


    protected int unescapeWorkerPosition(int escaped)
    {
        return escaped - 1;
    }
}

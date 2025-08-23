package de.hybris.platform.catalog.jalo.synchronization;

import org.apache.commons.collections.CollectionUtils;

public class ArrayBasedRelationLockHolder
{
    private final RelationAttributeInfo[] relationLocks;


    public ArrayBasedRelationLockHolder(int maxWorkers)
    {
        this.relationLocks = new RelationAttributeInfo[maxWorkers];
    }


    public synchronized boolean lockRelationAttribute(int workerNumber, RelationAttributeInfo lock, boolean block)
    {
        while(!tryToLockAttribute(lock, workerNumber))
        {
            if(!block)
            {
                return false;
            }
            try
            {
                wait();
            }
            catch(InterruptedException e)
            {
                return false;
            }
        }
        return true;
    }


    public synchronized void releaseAttributeLock(int workerNumber)
    {
        this.relationLocks[workerNumber] = null;
        notifyAll();
    }


    private final boolean tryToLockAttribute(RelationAttributeInfo lock, int workerPosition)
    {
        int relationLocksLength = this.relationLocks.length;
        for(int i = 0; i < relationLocksLength; i++)
        {
            if(i != workerPosition)
            {
                RelationAttributeInfo present = this.relationLocks[i];
                if(present != null && mustWaitOn(lock, present))
                {
                    return false;
                }
            }
        }
        this.relationLocks[workerPosition] = lock;
        return true;
    }


    protected boolean mustWaitOn(RelationAttributeInfo info1, RelationAttributeInfo info2)
    {
        boolean mustWait = false;
        if(info1.getRelationTypePK().equals(info2.getRelationTypePK()))
        {
            if(info1.isSource() != info2.isSource() && (info1
                            .getReferencedPKs().contains(info2.getParentPK()) || info2
                            .getReferencedPKs().contains(info1.getParentPK())))
            {
                mustWait = true;
            }
            else if(CollectionUtils.containsAny(info1.getReferencedPKs(), info2.getReferencedPKs()))
            {
                mustWait = true;
            }
        }
        return mustWait;
    }
}

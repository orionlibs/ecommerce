package de.hybris.platform.catalog.jalo.synchronization;

import org.apache.log4j.Logger;

public class ArrayBasedLockHolder extends AbstractWorkerLockHolder
{
    private static final Logger LOG = Logger.getLogger(ArrayBasedLockHolder.class.getName());
    private static final int DEFAULT_WORKER_STACK_SIZE = 10;
    private final int maxWorkers;
    private final long[][] itemCopyStacks;


    public ArrayBasedLockHolder(int maxWokers)
    {
        this(maxWokers, -1);
    }


    public ArrayBasedLockHolder(int maxWokers, int initialWorkerStackSize)
    {
        int stackSize = (initialWorkerStackSize > 0) ? initialWorkerStackSize : 10;
        this.maxWorkers = maxWokers;
        this.itemCopyStacks = new long[maxWokers][stackSize];
    }


    protected void doRelease(int workerNumber, long pk)
    {
        long[] stack = this.itemCopyStacks[workerNumber];
        for(int j = stack.length - 1; j >= 0; j--)
        {
            if(stack[j] != 0L)
            {
                if(stack[j] != pk)
                {
                    throw new IllegalStateException("cannot remove " + pk + " from copy stack - expecting " + stack[j] + "first!");
                }
                stack[j] = 0L;
                return;
            }
        }
        throw new IllegalStateException("cannot remove " + pk + " from copy stack - no item found!");
    }


    protected boolean doLock(long pk, int workerPosition)
    {
        if(hasExistingLock(pk, workerPosition))
        {
            return false;
        }
        if(!placeLock(pk, workerPosition))
        {
            growAndPlaceLock(pk, workerPosition);
        }
        return true;
    }


    protected boolean hasExistingLock(long pk, int workerPosition)
    {
        int numberOfMaxWorkers = this.maxWorkers;
        for(int i = 0; i < numberOfMaxWorkers; i++)
        {
            if(i != workerPosition)
            {
                long[] stack = this.itemCopyStacks[i];
                int stackLenght = stack.length;
                for(int j = 0; j < stackLenght; j++)
                {
                    if(stack[j] == pk)
                    {
                        return true;
                    }
                    if(stack[j] == 0L)
                    {
                        break;
                    }
                }
            }
        }
        return false;
    }


    protected boolean placeLock(long pk, int workerPosition)
    {
        long[] stack = this.itemCopyStacks[workerPosition];
        int stackLenght = stack.length;
        for(int i = 0; i < stackLenght; i++)
        {
            if(stack[i] == 0L)
            {
                stack[i] = pk;
                return true;
            }
        }
        return false;
    }


    protected void growAndPlaceLock(long pk, int workerPosition)
    {
        long[] stack = this.itemCopyStacks[workerPosition];
        int stackLenght = stack.length;
        long[] newStack = new long[stackLenght * 2];
        System.arraycopy(stack, 0, newStack, 0, stackLenght);
        newStack[stackLenght] = pk;
        this.itemCopyStacks[workerPosition] = newStack;
    }


    private final void printLocks()
    {
        StringBuilder stringBuilder = new StringBuilder();
        int itemCopyStacksLength = this.itemCopyStacks.length;
        for(int i = 0; i < itemCopyStacksLength; i++)
        {
            stringBuilder.append(i).append("->[");
            long[] stack = this.itemCopyStacks[i];
            int stackLenght = stack.length;
            for(int j = 0; j < stackLenght; j++)
            {
                if(stack[j] == 0L)
                {
                    break;
                }
                stringBuilder.append(stack[j]);
                if(j + 1 < stackLenght)
                {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("]\n");
        }
        LOG.error(stringBuilder.toString());
    }
}

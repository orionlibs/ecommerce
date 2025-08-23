package de.hybris.platform.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DeprecatedWorkerValueQueue<E> extends AbstractWorkerValueQueue<E>
{
    public static final int TIME_WAIT_PUT = 500;
    public static final TimeUnit TIME_WAIT_PUT_UNIT = TimeUnit.MILLISECONDS;
    private final int maxSize;
    private final ReadWriteLock valueQueueLock;
    private final Condition valueQueueEmpty;
    private final Condition valueQueueFull;
    private final LinkedList<E> queue;
    private final boolean[] currentlyTaken;
    private volatile List<E> takenValues;
    private volatile List<E> takenValuesForExec;
    private volatile boolean stopped = false;


    public DeprecatedWorkerValueQueue(int maxSize)
    {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
        this.takenValues = new ArrayList<>(30);
        this.takenValuesForExec = Collections.unmodifiableList(this.takenValues);
        this.currentlyTaken = new boolean[maxSize];
        this.valueQueueLock = new ReentrantReadWriteLock();
        this.valueQueueEmpty = this.valueQueueLock.writeLock().newCondition();
        this.valueQueueFull = this.valueQueueLock.writeLock().newCondition();
    }


    public Object executeOnTakenValues(WorkerValueQueue.ExecuteOnTaken<E> exec)
    {
        if(exec == null)
        {
            throw new IllegalArgumentException("exec was null");
        }
        this.valueQueueLock.readLock().lock();
        try
        {
            return exec.execute((WorkerValueQueue)this, this.takenValuesForExec);
        }
        finally
        {
            this.valueQueueLock.readLock().unlock();
        }
    }


    public void stop()
    {
        this.valueQueueLock.writeLock().lock();
        try
        {
            this.stopped = true;
        }
        finally
        {
            this.valueQueueEmpty.signalAll();
            this.valueQueueLock.writeLock().unlock();
        }
    }


    public E take(int workerNumber)
    {
        E ret = null;
        if(!this.stopped)
        {
            this.valueQueueLock.writeLock().lock();
            try
            {
                if(isValueTakenBy(workerNumber))
                {
                    throw new IllegalStateException("there is still a taken value for worker " + workerNumber);
                }
                while(!this.stopped && this.queue.isEmpty())
                {
                    this.valueQueueEmpty.await();
                }
                if(!this.stopped)
                {
                    ret = this.queue.remove();
                    setValueTakenBy(workerNumber, true, ret);
                }
            }
            catch(InterruptedException interruptedException)
            {
            }
            finally
            {
                this.valueQueueFull.signalAll();
                this.valueQueueLock.writeLock().unlock();
            }
        }
        return ret;
    }


    public void clearValueTaken(int workerNumber)
    {
        this.valueQueueLock.writeLock().lock();
        try
        {
            setValueTakenBy(workerNumber, false, null);
        }
        finally
        {
            this.valueQueueFull.signalAll();
            this.valueQueueLock.writeLock().unlock();
        }
    }


    public void put(E value)
    {
        put(value, null);
    }


    public boolean put(E value, WorkerValueQueue.ExecuteWhileWaiting<E> exec)
    {
        this.valueQueueLock.writeLock().lock();
        try
        {
            while(this.queue.size() >= this.maxSize)
            {
                try
                {
                    this.valueQueueFull.await(500L, TIME_WAIT_PUT_UNIT);
                }
                catch(InterruptedException interruptedException)
                {
                }
                if(!execute(exec, value))
                {
                    return false;
                }
            }
            this.queue.add(value);
            this.valueQueueEmpty.signalAll();
            return true;
        }
        finally
        {
            this.valueQueueLock.writeLock().unlock();
        }
    }


    public void waitUntilEmpty(long time, TimeUnit timeUnit, WorkerValueQueue.ExecuteWhileWaiting<E> exec)
    {
        while(waitIfNotEmpty(time, timeUnit))
        {
            if(!execute(exec, null))
            {
                return;
            }
        }
    }


    private final boolean waitIfNotEmpty(long time, TimeUnit timeUnit)
    {
        boolean keepWaiting;
        this.valueQueueLock.writeLock().lock();
        try
        {
            keepWaiting = isValueTakenOrQueueNotEmptyInternal();
            if(keepWaiting)
            {
                this.valueQueueFull.await(time, timeUnit);
                keepWaiting = isValueTakenOrQueueNotEmptyInternal();
            }
        }
        catch(InterruptedException e)
        {
            keepWaiting = false;
        }
        finally
        {
            this.valueQueueLock.writeLock().unlock();
        }
        return keepWaiting;
    }


    public void clear()
    {
        this.valueQueueLock.writeLock().lock();
        try
        {
            Arrays.fill(this.currentlyTaken, false);
            this.queue.clear();
            this.takenValues.clear();
        }
        finally
        {
            this.valueQueueFull.signalAll();
            this.valueQueueLock.writeLock().unlock();
        }
    }


    private boolean execute(WorkerValueQueue.ExecuteWhileWaiting<E> exec, E value)
    {
        boolean ret = true;
        if(exec != null)
        {
            ret = exec.execute((WorkerValueQueue)this, value);
        }
        return ret;
    }


    public boolean isValueTakenOrQueueNotEmpty()
    {
        this.valueQueueLock.readLock().lock();
        try
        {
            return isValueTakenOrQueueNotEmptyInternal();
        }
        finally
        {
            this.valueQueueLock.readLock().unlock();
        }
    }


    private final boolean isValueTakenOrQueueNotEmptyInternal()
    {
        return (!this.queue.isEmpty() || isValueTaken());
    }


    private boolean isValueTaken()
    {
        boolean result = false;
        for(boolean oneTaken : this.currentlyTaken)
        {
            result = (oneTaken || result);
        }
        return result;
    }


    private final boolean isValueTakenBy(int workerNumber)
    {
        return this.currentlyTaken[workerNumber];
    }


    private final void setValueTakenBy(int workerNumber, boolean taken, E value)
    {
        if(taken)
        {
            this.currentlyTaken[workerNumber] = true;
            assureValueSize(workerNumber);
            this.takenValues.set(workerNumber, value);
        }
        else
        {
            this.currentlyTaken[workerNumber] = false;
            if(this.takenValues.size() > workerNumber)
            {
                this.takenValues.set(workerNumber, null);
            }
        }
    }


    private final void assureValueSize(int pos)
    {
        int s = this.takenValues.size();
        if(s <= pos)
        {
            for(int i = s - 1; i <= pos; i++)
            {
                this.takenValues.add(null);
            }
        }
    }
}

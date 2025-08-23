package de.hybris.platform.util;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultWorkerValueQueue<E> extends AbstractWorkerValueQueue<E>
{
    public static final int DEFAULT_PUT_INTERVAL = 500;
    public static final TimeUnit DEFAULT_PUT_INTERVAL_UNIT = TimeUnit.MILLISECONDS;
    private final int workerSize;
    private final int maxQueueSize;
    private final ReadWriteLock valueQueueLock;
    private final Condition valueQueueEmpty;
    private final Condition valueQueueFull;
    private final Queue<E> queue;
    private volatile boolean stopped = false;
    private final AtomicReferenceArray<E> takenValues;
    private final TakenValueListAdapter<E> takenValuesListForExec;


    public DefaultWorkerValueQueue(int workerSize)
    {
        this(workerSize, -1);
    }


    public DefaultWorkerValueQueue(int workerSize, int maxQueueSize)
    {
        this.workerSize = workerSize;
        this.maxQueueSize = maxQueueSize;
        this.queue = new ConcurrentLinkedQueue<>();
        this.takenValues = new AtomicReferenceArray<>(workerSize);
        this.takenValuesListForExec = new TakenValueListAdapter(this.takenValues);
        this.valueQueueLock = new ReentrantReadWriteLock();
        this.valueQueueEmpty = this.valueQueueLock.writeLock().newCondition();
        this.valueQueueFull = this.valueQueueLock.writeLock().newCondition();
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


    public Object executeOnTakenValues(WorkerValueQueue.ExecuteOnTaken<E> exec)
    {
        if(exec == null)
        {
            throw new IllegalArgumentException("exec was null");
        }
        this.valueQueueLock.readLock().lock();
        try
        {
            return exec.execute((WorkerValueQueue)this, (List)this.takenValuesListForExec);
        }
        finally
        {
            this.valueQueueLock.readLock().unlock();
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
                while(!this.stopped && (ret = this.queue.poll()) == null)
                {
                    this.valueQueueEmpty.await();
                }
                if(ret != null)
                {
                    setValueTakenBy(workerNumber, ret);
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
            setValueTakenBy(workerNumber, null);
        }
        finally
        {
            this.valueQueueFull.signalAll();
            this.valueQueueLock.writeLock().unlock();
        }
    }


    public boolean put(E value, WorkerValueQueue.ExecuteWhileWaiting<E> exec)
    {
        this.valueQueueLock.writeLock().lock();
        try
        {
            if(this.stopped)
            {
                throw new IllegalStateException("queue " + this + " is already stopped");
            }
            while(this.maxQueueSize > 0 && this.queue.size() >= this.maxQueueSize)
            {
                if(!execute(exec, value))
                {
                    return false;
                }
                try
                {
                    this.valueQueueFull.await(500L, DEFAULT_PUT_INTERVAL_UNIT);
                }
                catch(InterruptedException interruptedException)
                {
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


    private boolean waitIfNotEmpty(long time, TimeUnit timeUnit)
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
            this.queue.clear();
            for(int i = 0; i < this.workerSize; i++)
            {
                this.takenValues.set(i, null);
            }
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


    private boolean isValueTakenOrQueueNotEmptyInternal()
    {
        return (!this.queue.isEmpty() || isValueTaken());
    }


    private boolean isValueTaken()
    {
        for(int i = 0; i < this.workerSize; i++)
        {
            if(isValueTakenBy(i))
            {
                return true;
            }
        }
        return false;
    }


    private boolean isValueTakenBy(int workerNumber)
    {
        return (this.takenValues.get(workerNumber) != null);
    }


    private void setValueTakenBy(int workerNumber, E value)
    {
        if(value == null)
        {
            this.takenValues.set(workerNumber, null);
        }
        else if(!this.takenValues.compareAndSet(workerNumber, null, value))
        {
            throw new IllegalStateException("there is already a taken value for worker " + workerNumber);
        }
    }
}

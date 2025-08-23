package de.hybris.platform.impex.jalo.imp;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ResultQueue<T>
{
    private final int maxSize;
    private final ReentrantLock resultListLock;
    private final Condition queueEmpty;
    private final Condition queueFull;
    private final LinkedList<T> queue;
    private static final boolean closed = false;
    public static final int TIME_WAIT_RESULT = 500;
    public static final TimeUnit TIME_WAIT_RESULT_UNIT = TimeUnit.MILLISECONDS;


    public ResultQueue(int maxSize)
    {
        this.queue = new LinkedList<>();
        this.maxSize = maxSize;
        this.resultListLock = new ReentrantLock();
        this.queueFull = this.resultListLock.newCondition();
        this.queueEmpty = this.resultListLock.newCondition();
    }


    public void put(T item) throws InterruptedException
    {
        this.resultListLock.lock();
        try
        {
            while(this.queue.size() >= this.maxSize)
            {
                this.queueFull.await();
            }
            this.queue.add(item);
        }
        finally
        {
            this.queueEmpty.signalAll();
            this.resultListLock.unlock();
        }
    }


    public T take(int time, TimeUnit timeUnit) throws InterruptedException
    {
        T ret = null;
        this.resultListLock.lock();
        try
        {
            if(this.queue.isEmpty())
            {
                this.queueEmpty.await(time, timeUnit);
            }
            if(!this.queue.isEmpty())
            {
                ret = this.queue.remove();
                this.queueFull.signalAll();
            }
        }
        finally
        {
            this.resultListLock.unlock();
        }
        return ret;
    }


    protected void close()
    {
        this.resultListLock.lock();
        try
        {
            this.queueEmpty.signalAll();
        }
        finally
        {
            this.resultListLock.unlock();
        }
    }
}

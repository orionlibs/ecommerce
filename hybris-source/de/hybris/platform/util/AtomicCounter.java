package de.hybris.platform.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter
{
    private final int startValue;
    private final int endValue;
    private final AtomicInteger counter;
    private final boolean allowOverflow;


    public String toString()
    {
        return "AtomicCounter[start:" + this.startValue + " end:" + this.endValue + " overflow:" + this.allowOverflow + " current:" + this.counter
                        .get() + "]";
    }


    public AtomicCounter()
    {
        this(0, 2147483647, false);
    }


    public AtomicCounter(int startValue)
    {
        this(startValue, 2147483647, false);
    }


    public AtomicCounter(int startValue, int endValue)
    {
        this(startValue, endValue, false);
    }


    public AtomicCounter(boolean allowOverflow)
    {
        this(0, 2147483647, allowOverflow);
    }


    public AtomicCounter(int startValue, boolean allowOverflow)
    {
        this(startValue, 2147483647, allowOverflow);
    }


    public AtomicCounter(int startValue, int endValue, boolean allowOverflow)
    {
        this.counter = new AtomicInteger(startValue);
        this.allowOverflow = allowOverflow;
        this.startValue = startValue;
        this.endValue = endValue;
    }


    public boolean allowsOverflow()
    {
        return this.allowOverflow;
    }


    public int getStartValue()
    {
        return this.startValue;
    }


    public int getEndValue()
    {
        return this.endValue;
    }


    public final int getCurrent()
    {
        return this.counter.get();
    }


    public final int generateNext()
    {
        int ret = this.counter.getAndIncrement();
        return isExhausted(ret) ? handleExhausted(ret) : ret;
    }


    private final boolean isExhausted(int current)
    {
        return (current >= this.endValue || current < this.startValue);
    }


    private final int handleExhausted(int current)
    {
        if(this.allowOverflow)
        {
            return resetCounter();
        }
        throw new IllegalStateException("counter " + this + " is exhausted (current is " + current + ")");
    }


    private final synchronized int resetCounter()
    {
        if(isExhausted(this.counter.get()))
        {
            this.counter.set(this.startValue + 1);
            return this.startValue;
        }
        return generateNext();
    }
}

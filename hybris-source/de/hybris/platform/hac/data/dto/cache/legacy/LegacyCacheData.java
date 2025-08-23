package de.hybris.platform.hac.data.dto.cache.legacy;

public class LegacyCacheData
{
    private int maxSize;
    private int currentSize;
    private int maxReachedSize;
    private long numHitsSinceStart;
    private long numAddsSinceStart;
    private long numDeletesSinceStart;
    private long numMissedSinceStart;


    public int getMaxSize()
    {
        return this.maxSize;
    }


    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }


    public int getCurrentSize()
    {
        return this.currentSize;
    }


    public void setCurrentSize(int currentSize)
    {
        this.currentSize = currentSize;
    }


    public int getMaxReachedSize()
    {
        return this.maxReachedSize;
    }


    public void setMaxReachedSize(int maxReachedSize)
    {
        this.maxReachedSize = maxReachedSize;
    }


    public long getNumHitsSinceStart()
    {
        return this.numHitsSinceStart;
    }


    public void setNumHitsSinceStart(long numHitsSinceStart)
    {
        this.numHitsSinceStart = numHitsSinceStart;
    }


    public long getNumAddsSinceStart()
    {
        return this.numAddsSinceStart;
    }


    public void setNumAddsSinceStart(long numAddsSinceStart)
    {
        this.numAddsSinceStart = numAddsSinceStart;
    }


    public long getNumDeletesSinceStart()
    {
        return this.numDeletesSinceStart;
    }


    public void setNumDeletesSinceStart(long numDeletesSinceStart)
    {
        this.numDeletesSinceStart = numDeletesSinceStart;
    }


    public long getNumMissedSinceStart()
    {
        return this.numMissedSinceStart;
    }


    public void setNumMissedSinceStart(long numMissedSinceStart)
    {
        this.numMissedSinceStart = numMissedSinceStart;
    }
}

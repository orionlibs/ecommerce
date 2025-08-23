package de.hybris.platform.jdbcwrapper;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnectionStatus
{
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile boolean testedConnectionOnce;
    private volatile long errorCounter;
    private volatile long errorCounterOnLastSuccessfulConnection;
    private volatile long lastErrorTime;


    public ConnectionStatus()
    {
        this.testedConnectionOnce = false;
        this.errorCounter = 0L;
        this.errorCounterOnLastSuccessfulConnection = 0L;
        this.lastErrorTime = -1L;
    }


    public long getLastErrorTime()
    {
        this.lock.readLock().lock();
        long value = this.lastErrorTime;
        this.lock.readLock().unlock();
        return value;
    }


    public boolean hadError()
    {
        this.lock.writeLock().lock();
        boolean hadError = (this.errorCounter > this.errorCounterOnLastSuccessfulConnection);
        this.lock.writeLock().unlock();
        return hadError;
    }


    public long getErrorCounter()
    {
        this.lock.readLock().lock();
        long localErrorCounter = this.errorCounter;
        this.lock.readLock().unlock();
        return localErrorCounter;
    }


    public void notifyConnectionError()
    {
        incrementErrorCounter();
    }


    public boolean wasConnectionTestedOnce()
    {
        this.lock.readLock().lock();
        boolean value = this.testedConnectionOnce;
        this.lock.readLock().unlock();
        return value;
    }


    public void notifyTestedConnectionOnce()
    {
        this.lock.writeLock().lock();
        this.testedConnectionOnce = true;
        this.lock.writeLock().unlock();
    }


    public void resetError()
    {
        this.lock.writeLock().lock();
        this.errorCounterOnLastSuccessfulConnection = this.errorCounter;
        this.lastErrorTime = -1L;
        this.lock.writeLock().unlock();
    }


    public void logError()
    {
        incrementErrorCounter();
        this.lastErrorTime = System.currentTimeMillis();
    }


    public void logSystemIsSuspended()
    {
        incrementErrorCounter();
        this.lastErrorTime = -1L;
    }


    private void incrementErrorCounter()
    {
        this.lock.writeLock().lock();
        long localErrorCounter = this.errorCounter;
        this.errorCounter = ++localErrorCounter;
        this.lock.writeLock().unlock();
    }


    protected ConnectionStatusInfo getConnectionStatusInfo()
    {
        this.lock.readLock().lock();
        ConnectionStatusInfo info = new ConnectionStatusInfo(this, hadError(), (this.lastErrorTime > -1L));
        this.lock.readLock().unlock();
        return info;
    }
}

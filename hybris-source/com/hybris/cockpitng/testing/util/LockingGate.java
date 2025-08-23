/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util;

import java.util.concurrent.locks.LockSupport;

/**
 * A gate that allow easy awaiting and locking in multithread tests.
 */
public class LockingGate
{
    private Thread unlocker;
    private Thread locker;


    /**
     * Gets a thread that is expected to call {@link #unlock()} method
     *
     * @return thread to unlock gate
     */
    public synchronized Thread getUnlockingThread()
    {
        return unlocker;
    }


    /**
     * Gets a thread that is expected to call {@link #lock()} method
     *
     * @return thread to lock gate
     */
    public synchronized Thread getLockingThread()
    {
        return locker;
    }


    /**
     * Marks current thread as the one to call {@link #lock()} method
     */
    public synchronized void registerLockingThread()
    {
        this.locker = Thread.currentThread();
    }


    /**
     * Marks current thread as the one to call {@link #unlock()} method
     */
    public synchronized void registerUnlockingThread()
    {
        this.unlocker = Thread.currentThread();
    }


    /**
     * Awaits until gate is locked - some thread calls {@link #lock()} method. Method returns immediately, if gate is
     * already locked. Current thread is considered as the one to call {@link #unlock()} method.
     *
     * @see #registerUnlockingThread()
     */
    public void assureLocked()
    {
        assureInUnlockingThread();
        LockSupport.park();
    }


    /**
     * Locks this gate. If unlocker is already waiting for gate to be locked (see {@link #assureLocked()}), it is released.
     * Method return immediately, if gate was already unlocked (see {@link #unlock()}) by other thread - otherwise current
     * thread awaits until gate gets unlocked.
     *
     * @see #registerLockingThread()
     */
    public void lock()
    {
        assureInLockingThread();
        LockSupport.unpark(assureUnlockingThreadRegistered());
        LockSupport.park();
    }


    protected void assureInLockingThread()
    {
        final Thread lockingThread = getLockingThread();
        if(lockingThread != null && lockingThread != Thread.currentThread())
        {
            throw new IllegalStateException("Different thread was registered as locking: " + lockingThread);
        }
        registerLockingThread();
    }


    protected Thread assureUnlockingThreadRegistered()
    {
        final Thread unlockingThread = getUnlockingThread();
        if(unlockingThread == null)
        {
            throw new IllegalStateException("No thread registered as unlocking. Call #registerUnlockingThread before locking!");
        }
        return unlockingThread;
    }


    /**
     * Unlocks this gate. If a locking thread is already waiting for gate to be unlocked (see {@link #lock()}), it is
     * released.
     */
    public void unlock()
    {
        assureInUnlockingThread();
        LockSupport.unpark(assureLockingThreadRegistered());
    }


    protected void assureInUnlockingThread()
    {
        final Thread unlockingThread = getUnlockingThread();
        if(unlockingThread != null && unlockingThread != Thread.currentThread())
        {
            throw new IllegalStateException("Different thread was registered as unlocking: " + unlockingThread);
        }
        registerUnlockingThread();
    }


    protected Thread assureLockingThreadRegistered()
    {
        final Thread lockingThread = getLockingThread();
        if(lockingThread == null)
        {
            throw new IllegalStateException("No thread registered as locking. Call #registerLockingThread before unlocking!");
        }
        return lockingThread;
    }
}

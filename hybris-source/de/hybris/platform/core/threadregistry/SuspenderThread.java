package de.hybris.platform.core.threadregistry;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.suspend.SuspendOptions;
import de.hybris.platform.util.RedeployUtilities;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SuspenderThread extends RegistrableThread
{
    private static final Logger LOG = LoggerFactory.getLogger(SuspenderThread.class);
    private static final int WAIT_TIME_MILLIS = 1000;
    private final SuspendOptions suspendOptions;
    private final AtomicBoolean stopRequested = new AtomicBoolean(false);
    private final CountDownLatch threadStartedLatch = new CountDownLatch(1);
    private final ThreadRegistry threadRegistry;
    private final AtomicBoolean allButCallerThreadSuspended = new AtomicBoolean(false);
    private volatile int waitTime = 5000;
    final SuspendResumeLoggerThread loggerThread;


    public SuspenderThread(SuspendOptions suspendOptions, ThreadRegistry threadRegistry)
    {
        super(SuspenderThread.class.getSimpleName());
        Objects.requireNonNull(suspendOptions, "suspendOptions mustn't be null");
        Objects.requireNonNull(threadRegistry, "threadRegistry mustn't be null");
        this.suspendOptions = suspendOptions;
        this.threadRegistry = threadRegistry;
        withInitialInfo(OperationInfo.builder()
                        .withCategory(OperationInfo.Category.SYSTEM)
                        .withStatusInfo("Waiting for system to be suspended.")
                        .asNotSuspendableOperation().build());
        usingThreadRegistry(threadRegistry);
        this.loggerThread = new SuspendResumeLoggerThread(threadRegistry);
        this.loggerThread.usingThreadRegistry(threadRegistry);
    }


    public void setWaitTime(int waitTime)
    {
        this.waitTime = Math.min(Math.max(100, waitTime), 10000);
    }


    public void startAndWaitForThreadToBeRunning()
    {
        this.loggerThread.startAndWaitForThreadToBeRunning();
        start();
        try
        {
            LOG.info("Waiting for {} to be running.", getClass().getSimpleName());
            this.threadStartedLatch.await();
            LOG.info("{} is running.", getClass().getSimpleName());
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOG.warn("Waiting has been interruted", e);
        }
    }


    public boolean waitForSuspensionOfAllButCallerThread(int timeout)
    {
        long timeoutTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeout);
        do
        {
            if(this.allButCallerThreadSuspended.get())
            {
                return true;
            }
        }
        while(System.currentTimeMillis() <= timeoutTime);
        return false;
    }


    void stopAndWaitForThreadToBeFinished()
    {
        this.stopRequested.set(true);
        try
        {
            LOG.info("Waiting for {} to finish.", getClass().getSimpleName());
            join();
            LOG.info("{} finished.", getClass().getSimpleName());
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOG.warn("Waiting has been interruted", e);
        }
    }


    protected void internalRun()
    {
        LOG.info("{} has started at {}.", getClass().getSimpleName(), new Date());
        while(this.threadRegistry.isInitOrUpdate())
        {
            try
            {
                LOG.info("Waiting 1 second for update to finish.");
                Thread.sleep(1000L);
            }
            catch(InterruptedException interruptedException)
            {
            }
        }
        this.threadStartedLatch.countDown();
        try
        {
            waitForSystemToBeSuspended();
            LOG.info("{} has finished without unexpected errors.", getClass().getSimpleName());
        }
        catch(Throwable t)
        {
            LOG.error("{} has finished with error.", getClass().getSimpleName(), t);
            throw t;
        }
        this.loggerThread.stopLogigng();
    }


    private void waitForSystemToBeSuspended()
    {
        long numberOfAttempts = 0L;
        while(true)
        {
            numberOfAttempts++;
            updateStatus(numberOfAttempts);
            try
            {
                waitBeforeNextAttempt();
                if(hasBeenStopped())
                {
                    LOG.info("{} has been stopped.", getClass().getSimpleName());
                    break;
                }
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                LOG.warn("{} has been interrupted.", getClass().getSimpleName());
                break;
            }
            if(systemCanBeSuspended())
            {
                LOG.info("System can be suspended. Finishing {}", getClass().getSimpleName());
                systemIsAboutToSuspend();
                break;
            }
        }
    }


    private void updateStatus(long attemptNumber)
    {
        OperationInfo.updateThread(
                        OperationInfo.builder().withAdditionalAttribute("attemptNumber", Long.toString(attemptNumber)).build(), this.threadRegistry);
    }


    private void waitBeforeNextAttempt() throws InterruptedException
    {
        int count = this.waitTime / 100 + 1;
        while(count-- > 0)
        {
            Thread.sleep(100L);
            if(hasBeenStopped())
            {
                break;
            }
        }
    }


    private boolean systemCanBeSuspended()
    {
        Set<Long> ids = this.threadRegistry.getAllNotSuspendableThreadIds();
        LOG.debug("Checking if system can be suspended. Not suspendable threads: {}. {}: {}", new Object[] {ids, getClass().getSimpleName(),
                        Long.valueOf(getId())});
        if(!this.allButCallerThreadSuspended.get() && areAllThreadsExceptCallerStopped(ids))
        {
            LOG.info("All but suspend invoking thread are suspended");
            this.allButCallerThreadSuspended.set(true);
        }
        Preconditions.checkState(!ids.isEmpty(), "Unexpected state: There should be at least one not suspendable thread.");
        if(ids.size() > 1)
        {
            return false;
        }
        Preconditions.checkState(ids.contains(Long.valueOf(getId())), "Unexpected state: Single unsuspendable thread should be %s",
                        getClass().getSimpleName());
        return true;
    }


    private boolean areAllThreadsExceptCallerStopped(Set<Long> ids)
    {
        Set<Long> threadIds = new HashSet<>(ids);
        this.suspendOptions.getSuspendingThreadId().ifPresent(i -> threadIds.remove(i));
        return (threadIds.size() <= 1);
    }


    private void systemIsAboutToSuspend()
    {
        if(this.suspendOptions.isShutdownWhenSuspended())
        {
            LOG.info("Shutting down the system");
            try
            {
                RedeployUtilities.shutdown();
            }
            catch(Throwable t)
            {
                LOG.info(t.getMessage(), t);
            }
            System.exit(0);
        }
    }


    private boolean hasBeenStopped()
    {
        return this.stopRequested.get();
    }
}

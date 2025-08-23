package de.hybris.platform.core.threadregistry;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.suspend.ResumeOptions;
import de.hybris.platform.core.suspend.ResumeTokenVerificationFailed;
import de.hybris.platform.core.suspend.SuspendOptions;
import de.hybris.platform.core.suspend.SuspendResult;
import de.hybris.platform.core.suspend.SuspendResumeService;
import de.hybris.platform.core.suspend.SystemState;
import de.hybris.platform.core.suspend.SystemStatus;
import de.hybris.platform.util.Token;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSuspendResumeService implements SuspendResumeService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSuspendResumeService.class);
    private final ThreadRegistry threadRegistry;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private volatile SuspenderThread suspenderThread;
    private volatile Token resumeToken;


    public DefaultSuspendResumeService()
    {
        this(SuspendResumeServices.getInstance().getThreadRegistry());
    }


    DefaultSuspendResumeService(ThreadRegistry threadRegistry)
    {
        this.threadRegistry = Objects.<ThreadRegistry>requireNonNull(threadRegistry);
    }


    public SuspendResult suspend(SuspendOptions suspendOptions)
    {
        Lock lock = this.readWriteLock.writeLock();
        lock.lock();
        try
        {
            SystemStatus currentStatus = getSystemStatus();
            if(!currentStatus.isRunningOrWaitingForUpdate())
            {
                LOG.info("Requested suspend but system is already {}", currentStatus);
                return DefaultSuspendResult.systemIsSuspendedOrWaiting(currentStatus);
            }
            LOG.info("Requesting suspend.");
            LOG.info("Starting new {}", SuspenderThread.class.getSimpleName());
            this.suspenderThread = new SuspenderThread(suspendOptions, this.threadRegistry);
            this.suspenderThread.startAndWaitForThreadToBeRunning();
            LOG.info("Blocking not suspendable operations");
            this.threadRegistry.blockNotSuspendableOperations();
            this.resumeToken = Token.generateNew();
            if(suspendOptions.isWaitForSuspend())
            {
                int timeout = Registry.getCurrentTenant().getConfig().getInt("suspend.shutdown.timeout", 30);
                LOG.info("Waiting for suspension of all but caller threads, timeout: {}", Integer.valueOf(timeout));
                boolean syncSuspendSuccessful = this.suspenderThread.waitForSuspensionOfAllButCallerThread(timeout);
                if(syncSuspendSuccessful)
                {
                    LOG.info("Successfully waited for all but caller thread to finish.");
                }
                else
                {
                    LOG.warn("Couldn't suspend synchronously all threads within requested timeout.");
                }
            }
            return DefaultSuspendResult.systemHasBeenRequestedToSuspend(getSystemStatus(), this.resumeToken.stringValue());
        }
        finally
        {
            lock.unlock();
        }
    }


    public void resume(ResumeOptions resumeOptions) throws ResumeTokenVerificationFailed
    {
        Lock lock = this.readWriteLock.writeLock();
        lock.lock();
        try
        {
            if(getSystemStatus() == SystemStatus.RUNNING)
            {
                LOG.info("System is already in {} state", SystemStatus.RUNNING);
                return;
            }
            LOG.info("Resuming system");
            LOG.info("Veryfing token");
            if(!this.resumeToken.verify(resumeOptions.getResumeToken()))
            {
                LOG.warn("Verification failed. System won't be resumed. Current status: {}", getSystemStatus());
                throw new ResumeTokenVerificationFailed();
            }
            LOG.info("Stopping {}", SuspenderThread.class.getSimpleName());
            this.suspenderThread.stopAndWaitForThreadToBeFinished();
            LOG.info("Unlocking not suspendable operations");
            this.threadRegistry.unblockNotSuspendableOperations();
            this.suspenderThread = null;
            this.resumeToken = null;
            waitForMasterTenantToBecomeAccessible();
            LOG.info("System has been resumed. Current status: {}", getSystemStatus());
        }
        finally
        {
            lock.unlock();
        }
    }


    public SystemState getSystemState()
    {
        Lock lock = this.readWriteLock.readLock();
        lock.lock();
        try
        {
            DefaultSystemState.Builder resultBuilder = DefaultSystemState.builder();
            resultBuilder.withStatus(getSystemStatus());
            Objects.requireNonNull(resultBuilder);
            this.threadRegistry.getAllOperations().values().forEach(resultBuilder::addThreadInfo);
            return (SystemState)resultBuilder.build();
        }
        finally
        {
            lock.unlock();
        }
    }


    public SystemStatus getSystemStatus()
    {
        return this.threadRegistry.getStatus();
    }


    @Nullable
    public String getResumeToken()
    {
        return (this.resumeToken == null) ? null : this.resumeToken.stringValue();
    }


    void waitForSuspenderThread() throws InterruptedException
    {
        this.suspenderThread.join();
    }


    private void waitForMasterTenantToBecomeAccessible()
    {
        LOG.info("Waiting for MasterTenant to become available.");
        for(int i = 0; i < getConfiguredWaitSeconds(); i++)
        {
            if(i > 0)
            {
                try
                {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException e)
                {
                    LOG.info("Waiting has been interrupted.");
                    Thread.currentThread().interrupt();
                    return;
                }
                LOG.info("MasterTenant is not available yet. Attempt #{} will be taken.", Integer.valueOf(i));
            }
            if(isMasterTenantAvailable())
            {
                LOG.info("MasterTenant is available.");
                return;
            }
        }
        LOG.warn("Giving up waiting for MasterTenant to become available.");
    }


    private boolean isMasterTenantAvailable()
    {
        try
        {
            return !Registry.getMasterTenant().cannotConnect();
        }
        catch(de.hybris.platform.core.AbstractTenant.TenantNotYetStartedException ex)
        {
            LOG.debug(ex.getMessage(), (Throwable)ex);
            return false;
        }
    }


    private int getConfiguredWaitSeconds()
    {
        return Registry.getMasterTenant().getConfig().getInt("master.tenant.resume.delay.seconds", 60);
    }
}

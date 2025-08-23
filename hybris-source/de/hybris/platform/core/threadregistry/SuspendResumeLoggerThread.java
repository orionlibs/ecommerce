package de.hybris.platform.core.threadregistry;

import de.hybris.platform.core.Registry;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuspendResumeLoggerThread extends RegistrableThread
{
    static final long PAUSE_MILLIS_REGISTERED_THREADS = Registry.getCurrentTenant()
                    .getConfig()
                    .getLong("threadregistry.pause.registeredthreads", 60000L);
    static final long PAUSE_MILLIS_THREADS_DUMP = Registry.getCurrentTenant()
                    .getConfig()
                    .getLong("threadregistry.pause.threadsdump", 600000L);
    private static final Logger LOG = LoggerFactory.getLogger(SuspendResumeLoggerThread.class);
    private Long lastThreadDump;
    private Long lastRegisteredThreads;
    private volatile boolean continueLogging;
    private final CountDownLatch threadStartedLatch = new CountDownLatch(1);
    private final ThreadRegistry threadRegistry;


    public SuspendResumeLoggerThread(ThreadRegistry threadRegistry)
    {
        super(SuspendResumeLoggerThread.class.getSimpleName());
        this.threadRegistry = threadRegistry;
        this.lastRegisteredThreads = Long.valueOf(System.currentTimeMillis());
        this.lastThreadDump = this.lastRegisteredThreads;
        this.continueLogging = true;
        updateThreadInfo();
    }


    public void stopLogigng()
    {
        this.continueLogging = false;
    }


    protected void startAndWaitForThreadToBeRunning()
    {
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


    protected void internalRun()
    {
        this.threadStartedLatch.countDown();
        while(this.continueLogging)
        {
            try
            {
                long now = System.currentTimeMillis();
                printRegisteredThreads(now);
                printThreadsDummp(now);
                Thread.sleep(100L);
            }
            catch(InterruptedException interruptedException)
            {
            }
        }
    }


    private void updateThreadInfo()
    {
        withInitialInfo(OperationInfo.builder()
                        .withCategory(OperationInfo.Category.SYSTEM)
                        .withStatusInfo("Logging thread dumps while waiting for suspend.")
                        .withThreadName(SuspendResumeLoggerThread.class
                                        .getSimpleName())
                        .asSuspendableOperation().build());
    }


    private void printRegisteredThreads(long now)
    {
        if(this.lastRegisteredThreads.longValue() + PAUSE_MILLIS_REGISTERED_THREADS <= now && LOG.isInfoEnabled())
        {
            StringBuilder threadsInfo = new StringBuilder();
            this.threadRegistry.getAllOperations().forEach((id, info) -> threadsInfo.append("" + info + "\n"));
            LOG.info("Registered threads at {} :\n\n{}", new Date(), threadsInfo);
            this.lastRegisteredThreads = Long.valueOf(now);
        }
    }


    private void printThreadsDummp(long now)
    {
        String text = "Thread dump: \n \n{}";
        if(this.lastThreadDump.longValue() + PAUSE_MILLIS_THREADS_DUMP <= now && LOG.isInfoEnabled())
        {
            LOG.info("Thread dump: \n \n{}", dumpThreadDump());
            this.lastThreadDump = Long.valueOf(now);
        }
    }


    public StringBuilder dumpThreadDump()
    {
        StringBuilder sb = new StringBuilder();
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        for(ThreadInfo ti : threadMxBean.dumpAllThreads(true, true))
        {
            sb.append(ti);
        }
        long[] deadlockedThreads = threadMxBean.findDeadlockedThreads();
        if(deadlockedThreads != null)
        {
            sb.append("Deadlocked threads:\n\n");
            ThreadInfo[] infos = threadMxBean.getThreadInfo(deadlockedThreads, true, true);
            for(ThreadInfo ti : infos)
            {
                sb.append(ti);
            }
        }
        return sb;
    }
}

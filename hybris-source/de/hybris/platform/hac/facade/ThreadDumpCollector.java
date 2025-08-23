package de.hybris.platform.hac.facade;

import de.hybris.platform.hac.exception.DumpNotAvailableException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.jgroups.util.ConcurrentLinkedBlockingQueue;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ThreadDumpCollector implements InitializingBean, DisposableBean
{
    private static final Logger LOG = Logger.getLogger(ThreadDumpCollector.class.getName());
    private static int INITIAL_DELAY = 0;
    @Value("${threaddumpcollector.maximumdumps}")
    int maxDumps;
    @Value("${threaddumpcollector.dumpExecutors}")
    int dumpExecutors;
    @Value("${threaddumpcollector.zipExecutors}")
    int zipExecutors;
    @Autowired
    ThreadMonitor threadMonitor;
    private DumpProcessorTask dumpProcessor;
    private ScheduledExecutorService dumpExecutorService;
    private ExecutorService zipExecutorService;
    private volatile ScheduledFuture<?> processingDumpFuture;


    public void afterPropertiesSet() throws Exception
    {
        ConcurrentLinkedBlockingQueue concurrentLinkedBlockingQueue = new ConcurrentLinkedBlockingQueue(this.maxDumps);
        this.dumpProcessor = new DumpProcessorTask(this, this.maxDumps, (BlockingQueue)concurrentLinkedBlockingQueue, this.threadMonitor);
        this.dumpExecutorService = Executors.newScheduledThreadPool(this.dumpExecutors);
        this.zipExecutorService = Executors.newFixedThreadPool(this.dumpExecutors);
    }


    public synchronized void startCollecting(long interval)
    {
        this.dumpProcessor.reset();
        if(!isDumpProcessing())
        {
            this.processingDumpFuture = this.dumpExecutorService.scheduleAtFixedRate((Runnable)this.dumpProcessor, INITIAL_DELAY, interval, TimeUnit.MILLISECONDS);
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("Dump processing already running " + this.processingDumpFuture);
        }
    }


    private boolean isDumpProcessing()
    {
        return (this.processingDumpFuture != null && !this.processingDumpFuture.isCancelled() && !this.processingDumpFuture.isDone());
    }


    public boolean isDumpAvailable()
    {
        return (this.dumpProcessor.currentDumps.get() > 0);
    }


    public synchronized Path stopCollecting() throws DumpNotAvailableException
    {
        if(this.processingDumpFuture != null)
        {
            this.processingDumpFuture.cancel(true);
        }
        return getResult();
    }


    public Path getResult()
    {
        try
        {
            DumpResult result = getOrCalculateResult();
            return result.getZipFile().toPath();
        }
        catch(CancellationException ce)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(ce);
            }
            unwrapCauseException(ce);
        }
        catch(ExecutionException ce)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(ce);
            }
            unwrapCauseException(ce);
        }
        catch(Exception e)
        {
            LOG.error(e);
        }
        return null;
    }


    private void unwrapCauseException(Throwable ce) throws DumpNotAvailableException, IllegalStateException
    {
        if(ce.getCause() instanceof DumpNotAvailableException)
        {
            throw (DumpNotAvailableException)ce.getCause();
        }
        throw new IllegalStateException(ce.getCause().getMessage(), ce.getCause());
    }


    DumpResult getOrCalculateResult() throws CancellationException, ExecutionException, InterruptedException
    {
        Future<DumpResult> zipFuture = this.dumpProcessor.getExecutedZipFuture();
        return zipFuture.get();
    }


    public boolean isCollecting()
    {
        return isDumpProcessing();
    }


    public void destroy() throws Exception
    {
        this.dumpExecutorService.shutdown();
        this.zipExecutorService.shutdown();
    }
}

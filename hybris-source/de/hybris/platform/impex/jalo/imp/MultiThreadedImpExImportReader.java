package de.hybris.platform.impex.jalo.imp;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.AbstractCodeLine;
import de.hybris.platform.impex.jalo.AbstractProcessResult;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.EOFProcessResult;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExLogFilter;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.InvalidHeaderPolicy;
import de.hybris.platform.impex.jalo.ItemProcessResult;
import de.hybris.platform.impex.jalo.PostProcessingFailedResult;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DefaultWorkerValueQueue;
import de.hybris.platform.util.WorkerValueQueue;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.Logger;

public class MultiThreadedImpExImportReader extends ImpExImportReader
{
    private static final Logger LOG = Logger.getLogger(MultiThreadedImpExImportReader.class.getName());
    private static final long WORKER_STARTUP_TIME = 10000L;
    private static final Item DUMMY = (Item)new TestItem();
    private static final int DEFAULT_VALUE_LINE_QUEUE_SIZE = 1000;
    private static final int DEFAULT_PROCESSED_QUEUE_SIZE = 5000;
    private static final int DEFAULT_RESULT_QUEUE_SIZE = 1000;
    private static final int DEFAULT_NUMBER_OF_RETRIES_TO_ACQUIRE_INITIAL_THREADS = 10;
    private volatile WorkerValueQueue<ValueLine> valueLineQueue;
    private LinkedBlockingQueue<ImpExWorkerResult> processedQueue;
    private ResultQueue resultQueue;
    private volatile boolean noMoreLines = false;
    private volatile boolean noMoreResults = false;
    private volatile int maxThreads = 4;
    private volatile int currentParallelWorkersLimit = 4;
    private volatile Semaphore parallelWorkersSemaphore = new Semaphore(4);
    private final List<ImpExWorker> _workers = new CopyOnWriteArrayList<>();
    private volatile ImpExReaderWorker readerWorker;
    private volatile ImpExResultProcessWorker resultProcessWorker;
    private volatile boolean parallelModeAllowed = true;
    private ImpExLogFilter logFilter = null;
    private final AtomicReference<InitialThreads> initialThreads = new AtomicReference<>();
    private final AtomicBoolean postProcessingFailed = new AtomicBoolean(false);
    private final WorkerValueQueue.ExecuteWhileWaiting<ValueLine> enqueueWaitingAction;
    private final WorkerValueQueue.ExecuteWhileWaiting<ValueLine> waitForEmptyQueueAction;


    public MultiThreadedImpExImportReader(String lines)
    {
        this(new CSVReader(new StringReader(lines)));
    }


    public MultiThreadedImpExImportReader(CSVReader reader)
    {
        this(reader, null);
    }


    public MultiThreadedImpExImportReader(CSVReader reader, boolean legacyMode)
    {
        this(reader, null, new MultiThreadedImportProcessor(), legacyMode);
    }


    public MultiThreadedImpExImportReader(CSVReader reader, CSVWriter dumpWriter)
    {
        this(reader, dumpWriter, new MultiThreadedImportProcessor());
    }


    public MultiThreadedImpExImportReader(CSVReader reader, CSVWriter dumpWriter, MultiThreadedImportProcessor processor)
    {
        this(reader, dumpWriter, new DocumentIDRegistry(), processor, ImpExManager.getImportStrictMode());
    }


    public MultiThreadedImpExImportReader(CSVReader reader, CSVWriter dumpWriter, MultiThreadedImportProcessor processor, boolean legacyMode)
    {
        super(reader, dumpWriter, new DocumentIDRegistry(), (ImportProcessor)processor, ImpExManager.getImportStrictMode(), legacyMode, InvalidHeaderPolicy.DUMP_VALUE_LINES);
        this.enqueueWaitingAction = (WorkerValueQueue.ExecuteWhileWaiting<ValueLine>)new Object(this);
        this.waitForEmptyQueueAction = (WorkerValueQueue.ExecuteWhileWaiting<ValueLine>)new Object(this);
    }


    public MultiThreadedImpExImportReader(CSVReader reader, CSVWriter dumpWriter, DocumentIDRegistry docIDRegistry, MultiThreadedImportProcessor processor, EnumerationValue mode)
    {
        super(reader, dumpWriter, docIDRegistry, (ImportProcessor)processor, mode, InvalidHeaderPolicy.DUMP_VALUE_LINES);
        this.enqueueWaitingAction = (WorkerValueQueue.ExecuteWhileWaiting<ValueLine>)new Object(this);
        this.waitForEmptyQueueAction = (WorkerValueQueue.ExecuteWhileWaiting<ValueLine>)new Object(this);
    }


    public void setDumpingAllowed(boolean dumpingAllowed)
    {
        if(!dumpingAllowed)
        {
            LOG.warn("cannot switch off dumping for multi-threaded import mode");
        }
    }


    public void discardNextLine()
    {
        if(isInParallelMode())
        {
            throw new UnsupportedOperationException("cannot call in parallel mode!");
        }
        super.discardNextLine();
    }


    public void dumpNextLine(String reason)
    {
        if(isInParallelMode())
        {
            throw new UnsupportedOperationException("cannot call in parallel mode!");
        }
        super.dumpNextLine(reason);
    }


    public Object readLine() throws ImpExException
    {
        Object ret = null;
        ensureAllThreads();
        if(!this.noMoreResults)
        {
            ensureReaderStarted();
            if(isInParallelMode())
            {
                ensureResultProcessorStarted();
            }
            do
            {
                try
                {
                    ret = getResultQueue().take(500, ResultQueue.TIME_WAIT_RESULT_UNIT);
                }
                catch(InterruptedException e)
                {
                    break;
                }
            }
            while(!isReaderFinished() && ret == null);
            if(ret == null || ret instanceof EOFProcessResult)
            {
                ensureReaderAndProcessorThreadsAreInPool();
                this.noMoreResults = true;
                ret = null;
            }
            else if(ret instanceof ItemProcessResult)
            {
                ret = ((ItemProcessResult)ret).getItem();
            }
            else
            {
                if(ret instanceof PostProcessingFailedResult)
                {
                    LOG.error("ImpEx import failed because of ImpExResultProcessWorker encountered an error.");
                    throw new ImpExException(((PostProcessingFailedResult)ret).getReason());
                }
                if(ret instanceof ImpExReadingFailedResult)
                {
                    LOG.error("ImpEx import failed because of ImpExReaderWorker encountered an error.");
                    throw new ImpExException(((ImpExReadingFailedResult)ret).getError());
                }
            }
        }
        return ret;
    }


    private void ensureReaderAndProcessorThreadsAreInPool()
    {
        boolean allFinished;
        do
        {
            allFinished = true;
            for(PoolableThread w : new PoolableThread[] {getInitialThreads().getReaderThread(), getInitialThreads().getProcessorThread()})
            {
                if(w != null && w.isBorrowed())
                {
                    allFinished = false;
                }
            }
            if(allFinished)
            {
                continue;
            }
            Thread.yield();
        }
        while(!allFinished);
    }


    private void ensureAllThreads()
    {
        getInitialThreads().ensureAllThreadsAreAllocated();
        if(isInParallelMode())
        {
            ensureWorkers(true);
        }
    }


    protected InitialThreads getInitialThreads()
    {
        InitialThreads result = this.initialThreads.get();
        if(result != null)
        {
            return result;
        }
        return this.initialThreads.updateAndGet(t -> (t == null) ? new InitialThreads(isInParallelMode(), this::tryToBorrowThread) : t);
    }


    protected final void postProcessValueLine(ValueLine currentValueLine, Item ret, Exception error) throws ImpExException
    {
        if(!isInParallelMode())
        {
            super.postProcessValueLine(currentValueLine, ret, error);
        }
    }


    protected final Item processLine(ValueLine valueLine) throws ImpExException
    {
        if(!isInParallelMode())
        {
            return super.processLine(valueLine);
        }
        if(valueLine != null)
        {
            enqueue(valueLine);
            return DUMMY;
        }
        return null;
    }


    protected void writeUserRightsLines() throws ImpExException
    {
        if(isInParallelMode())
        {
            waitForEmptyValueLineQueue();
        }
        super.writeUserRightsLines();
    }


    protected HeaderDescriptor createNewHeader(Map<Integer, String> line) throws HeaderValidationException
    {
        if(isInParallelMode())
        {
            waitForEmptyValueLineQueue();
        }
        return super.createNewHeader(line);
    }


    public void setCurrentHeader(HeaderDescriptor header)
    {
        if(getCurrentHeader() == null || !getCurrentHeader().equals(header))
        {
            setParallelModeAllowed(!"false".equalsIgnoreCase(header.getDescriptorData().getModifier("parallel")));
            super.setCurrentHeader(header);
        }
    }


    protected boolean readLineFromWorker() throws ImpExException
    {
        if(this.noMoreLines)
        {
            return false;
        }
        Object ret = null;
        try
        {
            if(shouldAbortDueToPostProcessingFailure())
            {
                LOG.error("Stopping reading left lines - ImpExResultProcessWorker encountered an error.");
            }
            else
            {
                ret = super.readLine();
            }
        }
        catch(RuntimeException e)
        {
            handleQueuesAndWorkers(null);
            this.noMoreLines = true;
            throw new ImpExException(e);
        }
        this.noMoreLines = (ret == null);
        if(this.noMoreLines)
        {
            handleQueuesAndWorkers(ret);
        }
        return (ret != null);
    }


    private boolean shouldAbortDueToPostProcessingFailure()
    {
        return this.postProcessingFailed.get();
    }


    private void handleQueuesAndWorkers(Object item) throws ImpExException
    {
        try
        {
            if(isInParallelMode())
            {
                if(item == null)
                {
                    waitForEmptyValueLineQueue();
                    stopValueLineQueue();
                    waitForWorkersToFinish();
                    getProcessedQueue().put(new EOFProcessResult());
                    waitForEmptyProcessedQueue();
                    getResultQueue().put(new EOFProcessResult());
                    close();
                }
            }
            else
            {
                getResultQueue().put(new ItemProcessResult((Item)item));
            }
        }
        catch(InterruptedException e)
        {
            throw new ImpExException(e, "error putting result into queue : " + e.getMessage(), 0);
        }
        catch(IOException e)
        {
            throw new ImpExException(e, "Couldn't close import reader: " + e.getMessage(), 0);
        }
    }


    protected boolean processPendingResult(ImpExWorkerResult result)
    {
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        ValueLine line = result.getLine();
        if(mustMarkLineAsUnresolved(result, line))
        {
            if(result.getError() != null)
            {
                line.markUnresolved(result.getError().getMessage());
            }
            else
            {
                line.markUnresolved();
            }
        }
        try
        {
            postProcessValueLineInternal(result.getLine(), (AbstractProcessResult)new ItemProcessResult(result.getResult()), result.getError());
        }
        catch(ImpExException | RuntimeException e)
        {
            LOG.error("error post-processing line " + result.getLine() + " : " + e.getMessage(), e);
            signalizePostProcessingWorkerFailure(result, e);
        }
        return true;
    }


    private void signalizePostProcessingWorkerFailure(ImpExWorkerResult result, Exception e)
    {
        this.postProcessingFailed.set(true);
        PostProcessingFailedResult postProcessingFailure = new PostProcessingFailedResult(result.getResult(), e);
        try
        {
            getResultQueue().put(postProcessingFailure);
        }
        catch(InterruptedException ee)
        {
            throw new SystemException("error putting result into queue : " + e.getMessage());
        }
    }


    protected boolean mustMarkLineAsUnresolved(ImpExWorkerResult result, ValueLine line)
    {
        return (result.getResult() == null && !line.isUnresolved() && !line.getHeader().isRemoveMode());
    }


    protected void postProcessValueLineInternal(ValueLine currentValueLine, AbstractProcessResult ret, Exception error) throws ImpExException
    {
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        super.postProcessValueLine(currentValueLine, ret.getItem(), error);
        if(ret.getItem() != null)
        {
            try
            {
                getResultQueue().put(ret);
            }
            catch(InterruptedException e)
            {
                throw new ImpExException(e, "error putting result into queue : " + e.getMessage(), 0);
            }
        }
    }


    protected boolean ensureValidHeaderOrMarkUnresolved(ValueLine valueLine)
    {
        boolean valid = super.ensureValidHeaderOrMarkUnresolved(valueLine);
        if(!valid && isInParallelMode())
        {
            handOffToResultProcessorWorker(new ImpExWorkerResult((ImpExWorker)this.readerWorker, getCurrentValueLine(), null, null));
        }
        return valid;
    }


    protected ValueLine fetchNextValueLine(ImpExWorker worker)
    {
        int number = worker.getNumber();
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        return (ValueLine)getValueLineQueue().take(number);
    }


    protected ImpExWorkerResult fetchNextWorkerResult()
    {
        ImpExWorkerResult resultToProcess;
        LinkedBlockingQueue<ImpExWorkerResult> queue = getProcessedQueue();
        try
        {
            do
            {
                resultToProcess = queue.poll(500L, DefaultWorkerValueQueue.DEFAULT_WAIT_INTERVAL_UNIT);
            }
            while(resultToProcess == null);
        }
        catch(InterruptedException e)
        {
            LOG.warn("Interupted while waiting for worker results");
            return null;
        }
        if(resultToProcess instanceof EOFProcessResult)
        {
            synchronized(getProcessedQueue())
            {
                queue.notifyAll();
            }
            return null;
        }
        return resultToProcess;
    }


    protected void addResult(ImpExWorkerResult result)
    {
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        int number = result.getWorker().getNumber();
        try
        {
            if(hasUnrecoverableError(result))
            {
                ValueLine line = result.getLine();
                line.markUnresolved();
                if(isDumpingAllowed())
                {
                    try
                    {
                        dumpUnresolvedLine(line);
                    }
                    catch(ImpExException e)
                    {
                        LOG.error("error dumping value line: " + line);
                    }
                }
                else
                {
                    LOG.error("cannot dump unresolved unrecoverable value line: " + line);
                }
                if(result.getError() != null)
                {
                    LOG.error("worker " + result.getWorker().getNumber() + " got error " + result.getError().getMessage(), result.getError());
                }
            }
            else
            {
                handOffToResultProcessorWorker(result);
            }
        }
        finally
        {
            getValueLineQueue().clearValueTaken(number);
        }
    }


    protected boolean hasUnrecoverableError(ImpExWorkerResult result)
    {
        return result.getLine().isUnrecoverable();
    }


    protected void handOffToResultProcessorWorker(ImpExWorkerResult result)
    {
        try
        {
            boolean success = false;
            while(!success)
            {
                success = getProcessedQueue().offer(result, 500L, DefaultWorkerValueQueue.DEFAULT_PUT_INTERVAL_UNIT);
            }
        }
        catch(InterruptedException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Interrupted while waiting to put worker result");
            }
        }
    }


    protected Item processValueLineFromWorker(ValueLine line) throws ImpExException
    {
        Semaphore s = this.parallelWorkersSemaphore;
        boolean acquired = false;
        try
        {
            s.acquire();
            acquired = true;
            return getImportProcessor().processItemData(line);
        }
        catch(InterruptedException e)
        {
            throw new ImpExException(e);
        }
        finally
        {
            if(acquired)
            {
                s.release();
            }
        }
    }


    private WorkerValueQueue<ValueLine> getValueLineQueue()
    {
        if(this.valueLineQueue == null)
        {
            synchronized(this)
            {
                if(this.valueLineQueue == null)
                {
                    int size = Config.getInt("impex.queue.valueline.size", 1000);
                    this.valueLineQueue = (WorkerValueQueue<ValueLine>)new DefaultWorkerValueQueue(getMaxThreads(), size);
                }
            }
        }
        return this.valueLineQueue;
    }


    private LinkedBlockingQueue<ImpExWorkerResult> getProcessedQueue()
    {
        if(this.processedQueue == null)
        {
            synchronized(this)
            {
                if(this.processedQueue == null)
                {
                    int size = Config.getInt("impex.queue.processed.size", 5000);
                    this.processedQueue = new LinkedBlockingQueue<>(size);
                }
            }
        }
        return this.processedQueue;
    }


    private ResultQueue getResultQueue()
    {
        if(this.resultQueue == null)
        {
            synchronized(this)
            {
                if(this.resultQueue == null)
                {
                    int size = Config.getInt("impex.queue.result.size", 1000);
                    this.resultQueue = new ResultQueue(size);
                }
            }
        }
        return this.resultQueue;
    }


    protected final boolean isInParallelMode()
    {
        return true;
    }


    protected void execute(AbstractCodeLine line, Map csvLine, boolean forEachMode) throws ImpExException
    {
        if(isInParallelMode() && !forEachMode)
        {
            waitForEmptyValueLineQueue();
        }
        super.execute(line, csvLine, forEachMode);
    }


    private void setParallelModeAllowed(boolean parallel)
    {
        if(this.parallelModeAllowed != parallel)
        {
            LOG.debug("changing parallel import mode from " + this.parallelModeAllowed + " to " + parallel + "...");
            if(parallel)
            {
                setMaxThreads(getMaxThreads());
            }
            else
            {
                setMaxThreads(1);
            }
            this.parallelModeAllowed = parallel;
        }
    }


    private final void ensureWorkers(boolean onlyInitial)
    {
        if(getAllocatedThreads() < getMaxThreads())
        {
            synchronized(this._workers)
            {
                if(getAllocatedThreads() < getMaxThreads())
                {
                    int added = 0;
                    if(getAllocatedThreads() == 0)
                    {
                        ((MultiThreadedImportProcessor)getImportProcessor()).setMaxThreads(getMaxThreads());
                        PoolableThread poolableThread = getInitialThreads().getWorkerThread();
                        Preconditions.checkState((poolableThread != null), "workerThread is null");
                        addNewWorker(poolableThread, 0);
                        added++;
                    }
                    if(onlyInitial)
                    {
                        Thread.yield();
                        return;
                    }
                    added += tryAllocateWorkers(getMaxThreads() - getAllocatedThreads());
                    Thread.yield();
                    LOG.debug("Allocated " + added + " new ones of " + getAllocatedThreads() + " ImpEx workers.");
                }
            }
        }
    }


    protected int tryAllocateWorkers(int amount)
    {
        int previousSize = this._workers.size();
        for(int i = 0; i < amount; i++)
        {
            PoolableThread poolableThread = tryToBorrowThread();
            if(poolableThread == null)
            {
                break;
            }
            int workerIndex = previousSize + i;
            addNewWorker(poolableThread, workerIndex);
        }
        return this._workers.size() - previousSize;
    }


    protected void addNewWorker(PoolableThread poolableThread, int workerIndex)
    {
        getValueLineQueue().clearValueTaken(workerIndex);
        ImpExWorker newOne = createWorker(poolableThread, workerIndex);
        newOne.start();
        this._workers.add(newOne);
        LOG.debug("added worker " + newOne);
    }


    protected ImpExWorker createWorker(PoolableThread poolableThread, int number)
    {
        return new ImpExWorker(poolableThread, this, number);
    }


    private final void ensureReaderStarted()
    {
        if(this.readerWorker == null)
        {
            synchronized(this)
            {
                if(this.readerWorker == null)
                {
                    PoolableThread poolableThread = getInitialThreads().getReaderThread();
                    Preconditions.checkState((poolableThread != null), "readerThread is null");
                    ImpExReaderWorker impExReaderWorker = new ImpExReaderWorker(poolableThread, this);
                    this.readerWorker = impExReaderWorker;
                    impExReaderWorker.start();
                    Thread.yield();
                    LOG.debug("Started ImpEx reader worker " + this.readerWorker);
                }
            }
        }
    }


    private final void ensureResultProcessorStarted()
    {
        if(this.resultProcessWorker == null)
        {
            synchronized(this)
            {
                if(this.resultProcessWorker == null)
                {
                    PoolableThread poolableThread = getInitialThreads().getProcessorThread();
                    Preconditions.checkState((poolableThread != null), "processorThread is null");
                    ImpExResultProcessWorker resultProcessor = new ImpExResultProcessWorker(poolableThread, this);
                    this.resultProcessWorker = resultProcessor;
                    resultProcessor.start();
                    Thread.yield();
                    LOG.debug("Started ImpEx result worker " + resultProcessor);
                }
            }
        }
    }


    protected PoolableThread tryToBorrowThread()
    {
        return tryToBorrowThread(Registry.getCurrentTenant().getWorkersThreadPool());
    }


    protected PoolableThread tryToBorrowThread(ThreadPool threadPool)
    {
        try
        {
            return threadPool.borrowThread();
        }
        catch(NoSuchElementException e)
        {
            return null;
        }
    }


    private final void enqueue(ValueLine line)
    {
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        ensureWorkers(false);
        getValueLineQueue().put(line, this.enqueueWaitingAction);
    }


    protected boolean isReaderFinished()
    {
        return this.readerWorker.isFinished();
    }


    protected boolean isResultProcessorFinished()
    {
        return this.resultProcessWorker.isFinished();
    }


    protected boolean isAllWorkerFinished()
    {
        long now = System.currentTimeMillis();
        for(ImpExWorker w : this._workers)
        {
            if(w != null && !w.isFinished())
            {
                if(!w.isStarted() && now - w.getCreationTimestamp() > 10000L)
                {
                    LOG.error("found worker which has not been started after 10000 ms : " + w);
                    continue;
                }
                return false;
            }
        }
        return true;
    }


    private void waitForEmptyValueLineQueue()
    {
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        getValueLineQueue().waitUntilEmpty(500L, DefaultWorkerValueQueue.DEFAULT_WAIT_INTERVAL_UNIT, this.waitForEmptyQueueAction);
    }


    private void stopValueLineQueue()
    {
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        getValueLineQueue().stop();
    }


    private void waitForEmptyProcessedQueue()
    {
        if(!isInParallelMode())
        {
            throw new IllegalStateException("cannot call in non-parallel mode!");
        }
        Queue<ImpExWorkerResult> queue = getProcessedQueue();
        try
        {
            synchronized(queue)
            {
                if(!queue.isEmpty())
                {
                    queue.wait();
                }
            }
        }
        catch(InterruptedException e)
        {
            LOG.warn("Interrupted while waiting for empty qeueue");
        }
    }


    private void waitForWorkersToFinish()
    {
        boolean allFinished;
        if(getValueLineQueue().isValueTakenOrQueueNotEmpty())
        {
            throw new IllegalStateException("line is still being processed or line queue is not empty");
        }
        do
        {
            allFinished = true;
            for(ImpExWorker w : this._workers)
            {
                if(w != null && !w.checkFinishedOrSkipStart())
                {
                    allFinished = false;
                }
            }
            if(allFinished)
            {
                continue;
            }
            Thread.yield();
        }
        while(!allFinished);
    }


    public ImpExLogFilter getLogFilter()
    {
        return this.logFilter;
    }


    public void setLogFilter(ImpExLogFilter logFilter)
    {
        this.logFilter = logFilter;
    }


    public int getMaxThreads()
    {
        return this.maxThreads;
    }


    public void setMaxThreads(int requested)
    {
        synchronized(this._workers)
        {
            if(getAllocatedThreads() > 0 || this.valueLineQueue != null)
            {
                limitParallelWorkers(requested);
            }
            else
            {
                this.maxThreads = requested;
                ((MultiThreadedImportProcessor)getImportProcessor()).setMaxThreads(this.maxThreads);
                this.currentParallelWorkersLimit = this.maxThreads;
                this.parallelWorkersSemaphore = new Semaphore(this.maxThreads);
            }
        }
    }


    private void limitParallelWorkers(int requested)
    {
        int requestedNumberOfWorkers;
        if(requested <= 1)
        {
            requestedNumberOfWorkers = 1;
        }
        else if(requested >= this.maxThreads)
        {
            requestedNumberOfWorkers = this.maxThreads;
        }
        else
        {
            requestedNumberOfWorkers = requested;
        }
        if(this.currentParallelWorkersLimit != requestedNumberOfWorkers)
        {
            this.currentParallelWorkersLimit = requestedNumberOfWorkers;
            this.parallelWorkersSemaphore = new Semaphore(requestedNumberOfWorkers);
            LOG.debug("Changing allowed parallel workers to " + requestedNumberOfWorkers);
        }
    }


    protected int getAllocatedThreads()
    {
        return this._workers.size();
    }


    public void addReadingError(ImpExException e, ImpExReaderWorker impExReaderWorker)
    {
        try
        {
            getResultQueue().put(new ImpExReadingFailedResult(impExReaderWorker, (Exception)e));
        }
        catch(InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}

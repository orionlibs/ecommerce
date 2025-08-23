package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DefaultWorkerValueQueue;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.WorkerValueQueue;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class CatalogVersionSyncMaster extends AbstractCatalogVersionSyncMaster
{
    private static final Logger LOG = Logger.getLogger(CatalogVersionSyncMaster.class.getName());
    private final boolean ignoreErrors;
    private volatile boolean errorsOccured = false;
    private volatile boolean finished = false;
    private final SyncStatusHolder statusWriter;
    private volatile SyncScheduleWriter dumpWriter = null;
    private volatile int lastMinLine = 0;
    private volatile int lastMaxLine = 0;
    private int turn;
    private final long statsInterval = Config.getInt("synchronization.loginterval", 60000);
    private final int turnsWithoutLocking = Config.getInt("catalog.sync.turns.without.locking", 1);
    private volatile long lastStatsTS = 0L;
    private int lastStatsMaxLine = 0;
    private int lastStatsLine = 0;
    private int lastStatsTurnTotal = 0;
    private int lastStatsTurn = 0;
    private int lastStatsDumpCount = 0;
    private final String crCode;
    private final WorkerValueQueue<SyncSchedule> scheduleQueue;
    private final int maxWorkers;
    private final CountDownLatch workersEndedSignal;
    private volatile CatalogVersionSyncWorkerThread[] workerThreads;
    private final Thread masterThread;
    private int firstTurnTotal = 0;
    private final ReentrantReadWriteLock exclusiveModeLock = new ReentrantReadWriteLock();
    private final CatalogVersionSyncProgressTracker progressTracker;


    public CatalogVersionSyncMaster(CatalogVersionSyncJob job, CatalogVersionSyncCronJob cronJob)
    {
        super(job, cronJob);
        this.doWhilePut = (WorkerValueQueue.ExecuteWhileWaiting<SyncSchedule>)new Object(this);
        this.doWhileWait = (WorkerValueQueue.ExecuteWhileWaiting<SyncSchedule>)new Object(this);
        this.progressTracker = new CatalogVersionSyncProgressTracker((CronJob)cronJob);
        this.masterThread = Thread.currentThread();
        int configuredWorkers = job.getMaxThreadsAsPrimitive();
        this.maxWorkers = configuredWorkers;
        this.workersEndedSignal = new CountDownLatch(this.maxWorkers);
        this.ignoreErrors = cronJob.isIgnoreErrors();
        this.scheduleQueue = (WorkerValueQueue<SyncSchedule>)new DefaultWorkerValueQueue(this.maxWorkers, 5000);
        this.statusWriter = cronJob.createStatusHolder();
        this.crCode = cronJob.getCode();
    }


    private final void ensureWorkersStarted()
    {
        if(this.workerThreads == null)
        {
            CountDownLatch workersStartSignal = null;
            synchronized(this)
            {
                if(this.workerThreads == null)
                {
                    workersStartSignal = new CountDownLatch(1);
                    CatalogVersionSyncWorkerThread[] threads = new CatalogVersionSyncWorkerThread[this.maxWorkers];
                    for(int i = 0; i < this.maxWorkers; i++)
                    {
                        threads[i] = new CatalogVersionSyncWorkerThread(createWorker(i), workersStartSignal, this.workersEndedSignal);
                        threads[i].start();
                    }
                    this.workerThreads = threads;
                }
            }
            if(workersStartSignal != null)
            {
                workersStartSignal.countDown();
            }
        }
    }


    CatalogVersionSyncWorker createWorker(int i)
    {
        return new CatalogVersionSyncWorker(this, "SyncWorker<" + this.crCode + " " + i + 1 + " of " + this.maxWorkers + ">", i);
    }


    private final WorkerValueQueue<SyncSchedule> getQueue()
    {
        return this.scheduleQueue;
    }


    public final boolean isIgnoreErrors()
    {
        return this.ignoreErrors;
    }


    public final SyncSchedule fetchNext(CatalogVersionSyncWorker worker) throws InterruptedException
    {
        if(this.finished || (!this.ignoreErrors && this.errorsOccured))
        {
            return null;
        }
        return (SyncSchedule)getQueue().take(worker.getWorkerNumber());
    }


    protected int getCurrentDumpCount()
    {
        return (this.dumpWriter != null) ? this.dumpWriter.getCount() : -1;
    }


    protected int getCurrentDumpDeadlockCount()
    {
        return (this.dumpWriter != null) ? this.dumpWriter.getDeadlockCount() : -1;
    }


    protected boolean notifyFinished(CatalogVersionSyncWorker worker, SyncSchedule schedule)
    {
        logLineFinished(schedule);
        logStats(false);
        getQueue().clearValueTaken(worker.getWorkerNumber());
        return !this.finished;
    }


    private static final WorkerValueQueue.ExecuteOnTaken<SyncSchedule> doGetMinTaken = (WorkerValueQueue.ExecuteOnTaken<SyncSchedule>)new Object();
    private final WorkerValueQueue.ExecuteWhileWaiting<SyncSchedule> doWhilePut;
    private final WorkerValueQueue.ExecuteWhileWaiting<SyncSchedule> doWhileWait;


    private final synchronized void logLineFinished(SyncSchedule finished)
    {
        int myNr = finished.getLineNumber();
        SyncSchedule minTaken = (SyncSchedule)getQueue().executeOnTakenValues(doGetMinTaken);
        this.lastMaxLine = Math.max(this.lastMaxLine, myNr);
        this.lastMinLine = (minTaken == null) ? this.lastMaxLine : (minTaken.getLineNumber() - 1);
        getStatusHandler().logLine(this.lastMinLine, getCurrentDumpCount(), this.errorsOccured);
    }


    private final void logStats(boolean forceLog)
    {
        if(LOG.isInfoEnabled())
        {
            long timeStamp = System.currentTimeMillis();
            if(forceLog || timeStamp > this.lastStatsTS + this.statsInterval)
            {
                synchronized(this)
                {
                    if(forceLog || timeStamp > this.lastStatsTS + this.statsInterval)
                    {
                        long delta = timeStamp - this.lastStatsTS;
                        this.lastStatsTS = timeStamp;
                        int maxDiff = this.lastMaxLine - this.lastStatsMaxLine;
                        this.lastStatsMaxLine = this.lastMaxLine;
                        int currentDumpCount = getCurrentDumpCount();
                        int currentDumpDeadlockCount = getCurrentDumpDeadlockCount();
                        int dumpDiff = currentDumpCount - this.lastStatsDumpCount;
                        this.lastStatsDumpCount = currentDumpCount;
                        if(LOG.isDebugEnabled() && this.workerThreads != null)
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Stats<").append(this.crCode);
                            stringBuilder.append(" maxLine:").append(this.lastMaxLine).append(" (+").append(maxDiff).append(")");
                            stringBuilder.append(" dumped:").append(currentDumpCount).append(" (+").append(dumpDiff).append(")");
                            long copyCacheHitsTotal = 0L;
                            long copyCacheMissesTotal = 0L;
                            for(int i = 0; i < this.workerThreads.length; i++)
                            {
                                copyCacheHitsTotal += this.workerThreads[i].getWorker().getCopyContext().getCopyCacheHits();
                                copyCacheMissesTotal += this.workerThreads[i].getWorker().getCopyContext().getCopyCacheMisses();
                            }
                            long all = copyCacheHitsTotal + copyCacheMissesTotal;
                            stringBuilder.append(" cache:").append((all > 0L) ? (int)(copyCacheHitsTotal * 100L / all) : 0).append("% (hits:").append(copyCacheHitsTotal).append(", misses:").append(copyCacheMissesTotal).append(" total)");
                            stringBuilder.append(">");
                            LOG.debug(stringBuilder.toString());
                        }
                        int lastTurnDumpCount = getStatusHandler().getLastTurnDumpCount();
                        int lastLine = getStatusHandler().getLastLine();
                        int thisTurnTotal = (this.turn == 1) ? lastLine : lastTurnDumpCount;
                        thisTurnTotal = (thisTurnTotal < 0) ? 0 : thisTurnTotal;
                        int thisMaxDiff = lastLine - ((this.turn > this.lastStatsTurn) ? 0 : this.lastStatsLine);
                        thisMaxDiff = (this.turn > this.lastStatsTurn) ? (thisMaxDiff + this.lastStatsTurnTotal - this.lastStatsLine) : thisMaxDiff;
                        thisMaxDiff = (thisMaxDiff < 0) ? 0 : thisMaxDiff;
                        String includedText = (thisMaxDiff > lastLine && this.turn > this.lastStatsTurn) ? " including previous turn" : "";
                        double rate = (delta > 0L) ? (thisMaxDiff * 1000.0D / delta) : 0.0D;
                        rate = (Double.isInfinite(rate) || Double.isNaN(rate)) ? 0.0D : rate;
                        int percentage = (thisTurnTotal > 0) ? (lastLine * 100 / thisTurnTotal) : 0;
                        this.lastStatsLine = lastLine;
                        this.lastStatsTurnTotal = thisTurnTotal;
                        this.lastStatsTurn = this.turn;
                        String info = String.format("%d. pass, %d (+%d%s) of %d items processed (%d %%),  %.2f items/sec, %d (+%d, deadlocks:%d) items dumped.",
                                        new Object[] {Integer.valueOf(this.turn), Integer.valueOf(lastLine), Integer.valueOf(thisMaxDiff), includedText, Integer.valueOf(thisTurnTotal), Integer.valueOf(percentage), Double.valueOf(rate), Integer.valueOf((currentDumpCount >= 0) ? currentDumpCount : 0),
                                                        Integer.valueOf((dumpDiff >= 0) ? dumpDiff : 0), Integer.valueOf((currentDumpDeadlockCount >= 0) ? currentDumpDeadlockCount : 0)});
                        getCronJob().setStatusMessage(info);
                        LOG.info(info);
                    }
                }
            }
        }
    }


    private final void closeDumpWriter(boolean store)
    {
        if(this.dumpWriter != null)
        {
            if(store)
            {
                getCronJob().storeDumpedSchedules(this.dumpWriter);
            }
            else
            {
                this.dumpWriter.closeQuietly();
            }
            this.dumpWriter = null;
        }
    }


    public void runExclusiveIfNecessary(SyncSchedule schedule, Runnable r)
    {
        if(getCurrentTurn() <= this.turnsWithoutLocking)
        {
            r.run();
            return;
        }
        Lock l = schedule.isDeadlockVictim() ? this.exclusiveModeLock.writeLock() : this.exclusiveModeLock.readLock();
        l.lock();
        try
        {
            r.run();
        }
        finally
        {
            l.unlock();
        }
    }


    public synchronized void dump(SyncSchedule record)
    {
        if(this.dumpWriter == null)
        {
            this.dumpWriter = getCronJob().createDumpScheduleWriter(getStatusHandler());
        }
        try
        {
            this.dumpWriter.write(record);
            incrementDumpedItemsCount(record.getWeight());
        }
        catch(IOException e)
        {
            throw new IllegalStateException("could not dump pending item", e);
        }
    }


    private final void enqueue(SyncSchedule schedule) throws AbortCronJobException
    {
        if(this.finished)
        {
            throw new IllegalStateException("master is already finished - cannot enqueue " + schedule);
        }
        ensureWorkersStarted();
        getQueue().put(schedule, this.doWhilePut);
        incrementScheduledItemsCount(schedule.getWeight());
    }


    protected void waitForWorkersToDie()
    {
        this.finished = true;
        if(this.workerThreads != null)
        {
            for(int i = 0; i < this.maxWorkers; i++)
            {
                CatalogVersionSyncWorkerThread catalogVersionSyncWorkerThread = this.workerThreads[i];
                if(catalogVersionSyncWorkerThread.isAlive())
                {
                    this.workerThreads[i].interrupt();
                }
            }
            try
            {
                this.workersEndedSignal.await();
            }
            catch(InterruptedException interruptedException)
            {
            }
        }
    }


    protected void waitForEmptyQueue()
    {
        getQueue().waitUntilEmpty(this.doWhileWait);
    }


    private final SyncStatusHolder getStatusHandler()
    {
        return this.statusWriter;
    }


    public void doSynchronization() throws AbortCronJobException
    {
        boolean syncDone = false;
        this.lastStatsTS = System.currentTimeMillis();
        getCronJob().setStatusMessage("Synchronizing...");
        try
        {
            if(getCronJob().hasSchedules())
            {
                initTurn(getStatusHandler().getTurn());
                int lastTurnDumpCount = -1;
                int currentDump = -1;
                do
                {
                    lastTurnDumpCount = currentDump;
                    currentDump = doSyncTurn();
                }
                while(currentDump > 0 && (lastTurnDumpCount == -1 || getCronJob().isLastDumpsDifferent()));
                this.errorsOccured |= (currentDump > 0) ? 1 : 0;
                getStatusHandler().syncDone();
                syncDone = true;
                waitForWorkersToDie();
                if(currentDump > 0 && LOG.isEnabledFor((Priority)Level.ERROR))
                {
                    LOG.error("sync ended with " + currentDump + " unfinished items - see last sync media for details");
                }
            }
        }
        finally
        {
            closeDumpWriter(false);
            if(!syncDone)
            {
                getStatusHandler().syncAborted();
                waitForWorkersToDie();
            }
            this.progressTracker.close();
        }
    }


    protected int doSyncTurn() throws AbortCronJobException
    {
        int currentDump = 0;
        SyncScheduleReader scheduleReader = null;
        try
        {
            scheduleReader = getCronJob().createSyncScheduleReader();
            if(getStatusHandler().getLastLine() > 0)
            {
                scheduleReader.setLinesToSkip(getStatusHandler().getLastLine());
                currentDump = getStatusHandler().getLastLineDumpCount();
            }
            else
            {
                getStatusHandler().logBeginTurn(getCurrentTurn());
                currentDump = 0;
            }
            int lastLine = getStatusHandler().getLastLine();
            this.lastStatsDumpCount = 0;
            this.lastStatsMaxLine = 0;
            Integer syncCronjobInvocationCount = (Integer)getCronJob().getProperty("syncCronjobInvocationCount");
            int invocationCount = (syncCronjobInvocationCount == null) ? 0 : syncCronjobInvocationCount.intValue();
            getCronJob().setProperty("syncCronjobInvocationCount", Integer.valueOf(invocationCount + 1));
            if(invocationCount == 0 && lastLine > 0)
            {
                LOG.error("unexpected lastLine (" + lastLine + ") for first sync invocation of " + getCronJob() + "[" +
                                getCronJob().getPK() + "]");
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Starting sync " +
                                getCronJob().getCode() + "[" + getCronJob().getPK() + "] (turn " + getCurrentTurn() + ", previous dump " +
                                getStatusHandler().getLastTurnDumpCount() + ", lines to skip " + lastLine + ", invocations " + invocationCount + ")");
            }
            doReadSchedules(scheduleReader, lastLine);
            boolean aborted = getCronJob().isRequestAbortAsPrimitive();
            if(!aborted)
            {
                waitForEmptyQueue();
            }
            if(!this.ignoreErrors && this.errorsOccured)
            {
                throw new RuntimeException("got error while synchronozing " + getJob().getCode() + " - see error log");
            }
            if(aborted)
            {
                throw new AbortCronJobException("abort requested");
            }
            logStats(true);
            currentDump = getCurrentDumpCount();
            closeDumpWriter(true);
            int previousDumpCount = getStatusHandler().getLastTurnDumpCount();
            getStatusHandler().logFinishTurn(getCurrentTurn(), currentDump);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Finished sync " + getCronJob().getCode() + " (turn " + getCurrentTurn() + ", lines " + lastLine + ", previous dump " + previousDumpCount + ", current dump " + currentDump + ")");
            }
            nextTurn();
            previousDumpCount = currentDump;
            return previousDumpCount;
        }
        finally
        {
            if(scheduleReader != null)
            {
                try
                {
                    scheduleReader.close();
                }
                catch(IOException iOException)
                {
                }
            }
        }
    }


    protected void doReadSchedules(SyncScheduleReader scheduleReader, int lastLine) throws AbortCronJobException
    {
        boolean aborted = false;
        int line = lastLine;
        while(!aborted && scheduleReader.readNextLine())
        {
            if(getCronJob().isRequestAbortAsPrimitive())
            {
                aborted = true;
                continue;
            }
            SyncSchedule scheduledItem = scheduleReader.getScheduleFromLine();
            line++;
            scheduledItem.setLineNumber(line);
            enqueue(scheduledItem);
            if(isAllWorkerDead())
            {
                LOG.error("all workers died - aborting sync ");
                aborted = true;
                continue;
            }
            aborted = (!this.ignoreErrors && this.errorsOccured);
            Thread.yield();
        }
    }


    protected void notifyError(CatalogVersionSyncWorker worker, SyncSchedule scheduledItem, Exception exception)
    {
        String errorTxt = "Caught unexpected error " + exception.getLocalizedMessage() + " synchronizing " + scheduledItem + "\n" + Utilities.getStackTraceAsString(exception);
        LOG.error("Got error from worker " + worker + " errorText is: " + errorTxt);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("worker " + worker.getWorkerNumber() + " reports error: " + exception.getMessage() + "\n" +
                            Utilities.getStackTraceAsString(exception));
        }
        this.errorsOccured = true;
        if(scheduledItem != null)
        {
            logLineFinished(scheduledItem);
        }
        getQueue().clearValueTaken(worker.getWorkerNumber());
        if(!isIgnoreErrors())
        {
            this.masterThread.interrupt();
        }
    }


    protected void notifyWorkerDied(CatalogVersionSyncWorker worker)
    {
        getQueue().clearValueTaken(worker.getWorkerNumber());
    }


    public boolean hasErrors()
    {
        return this.errorsOccured;
    }


    private boolean isAllWorkerDead()
    {
        for(int i = 0; i < this.maxWorkers; i++)
        {
            if(this.workerThreads[i].isAlive())
            {
                return false;
            }
        }
        return true;
    }


    private void initTurn(int set)
    {
        this.turn = set;
    }


    private void nextTurn()
    {
        this.turn++;
    }


    public int getCurrentTurn()
    {
        return this.turn;
    }


    protected void setFirstTurnTotal(int firstTurnTotal)
    {
        this.firstTurnTotal = firstTurnTotal;
    }


    public void incrementProcessedItemsCount(int weight)
    {
        this.progressTracker.incrementProcessedItemsCount(weight);
    }


    private void incrementDumpedItemsCount(int weight)
    {
        this.progressTracker.incrementDumpedItemsCount(weight);
    }


    private void incrementScheduledItemsCount(int weight)
    {
        this.progressTracker.incrementScheduledItemsCount(weight);
    }
}

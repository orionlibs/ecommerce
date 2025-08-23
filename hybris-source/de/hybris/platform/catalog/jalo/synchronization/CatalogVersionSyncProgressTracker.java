package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobHistory;
import de.hybris.platform.cronjob.jalo.CronJobProgressTracker;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogVersionSyncProgressTracker extends CronJobProgressTracker
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogVersionSyncProgressTracker.class);
    private final AtomicInteger processedItemsCount;
    private final AtomicInteger scheduledItemsCount;
    private final AtomicInteger dumpedItemsCount;
    private final AtomicInteger processedItems;
    private final AtomicInteger scheduledItems;
    private final AtomicInteger dumpedItems;
    private int lastProcessedItemsCount;


    public CatalogVersionSyncProgressTracker(CronJob cronJob)
    {
        super(cronJob);
        this.processedItemsCount = new AtomicInteger(0);
        this.scheduledItemsCount = new AtomicInteger(0);
        this.dumpedItemsCount = new AtomicInteger(0);
        this.processedItems = new AtomicInteger(0);
        this.scheduledItems = new AtomicInteger(0);
        this.dumpedItems = new AtomicInteger(0);
        this.lastProcessedItemsCount = 0;
    }


    public void incrementScheduledItemsCount(int value)
    {
        this.scheduledItems.addAndGet(value);
        this.scheduledItemsCount.incrementAndGet();
    }


    public void incrementDumpedItemsCount(int value)
    {
        this.dumpedItems.addAndGet(value);
        this.dumpedItemsCount.incrementAndGet();
    }


    public void incrementProcessedItemsCount(int value)
    {
        this.processedItems.addAndGet(value);
        this.processedItemsCount.incrementAndGet();
        setProgress(getProgress());
    }


    public Double getProgress()
    {
        return Double.valueOf(this.processedItems.doubleValue() / this.scheduledItems.doubleValue() * 100.0D);
    }


    private String getStatus()
    {
        Double speed = Double.valueOf((this.processedItemsCount.doubleValue() - this.lastProcessedItemsCount) /
                        Double.valueOf(getUpdateInterval(TimeUnit.SECONDS)).doubleValue());
        this.lastProcessedItemsCount = this.processedItemsCount.intValue();
        return String.format("scheduled items: %d, processed items: %d, speed %.2f items/s, dumped items: %d", new Object[] {Integer.valueOf(this.scheduledItemsCount.intValue()), Integer.valueOf(this.processedItemsCount.intValue()), speed,
                        Integer.valueOf(this.dumpedItemsCount.intValue())});
    }


    protected void storeCronJobHistoryInDB(CronJobHistory history, Double progress)
    {
        if(history == null)
        {
            return;
        }
        BiConsumer<CronJobHistory, Double> superStoreCronJobHistoryInDB = (h, p) -> super.storeCronJobHistoryInDB(h, p);
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, superStoreCronJobHistoryInDB, history, progress));
        }
        catch(Exception e)
        {
            logException(e);
            throw new SystemException(e);
        }
    }


    private void logException(Exception ex)
    {
        String msg = ex.getMessage();
        LOG.error(msg);
        LOG.debug(msg, ex);
    }
}

package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.persistence.hjmp.HJMPException;
import de.hybris.platform.util.Config;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public abstract class AbstractProgressTracker
{
    private final Logger LOG = Logger.getLogger(AbstractProgressTracker.class.getName());
    private volatile Double progress;
    private volatile DateTime lastUpdate;
    private final int updateIntervalInSeconds = Config.getInt("cronjob.progress.interval.seconds", 5);


    public AbstractProgressTracker()
    {
        setLastUpdate(new DateTime());
    }


    private void setLastUpdate(DateTime lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }


    public long getUpdateInterval(TimeUnit timeUnit)
    {
        return timeUnit.convert(this.updateIntervalInSeconds, TimeUnit.SECONDS);
    }


    public Double getProgress()
    {
        return this.progress;
    }


    public final void setProgress(Double progress)
    {
        this.progress = progress;
        if(this.lastUpdate.isBefore((ReadableInstant)(new DateTime()).minusSeconds(Long.valueOf(getUpdateInterval(TimeUnit.SECONDS)).intValue())))
        {
            storeProgressInDB_internal();
        }
    }


    private void storeProgressInDB_internal()
    {
        try
        {
            storeProgressInDB(this.progress);
            setLastUpdate(new DateTime());
        }
        catch(HJMPException exception)
        {
            if(this.LOG.isDebugEnabled() && exception.getCausedByException() instanceof de.hybris.platform.persistence.hjmp.HybrisOptimisticLockingFailureException)
            {
                this.LOG.debug("skipping parallel updates on ProgressTracker object");
            }
        }
    }


    abstract void storeProgressInDB(Double paramDouble);


    public void close()
    {
        storeProgressInDB_internal();
    }


    public String toString()
    {
        return String.format("progress: %2.0f%%", new Object[] {this.progress});
    }
}

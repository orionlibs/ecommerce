package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.jalo.ImpExLogFilter;
import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.threadpool.NamedProcess;
import de.hybris.platform.util.threadpool.PoolableThread;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

public class ImpExWorker implements Runnable, NamedProcess, ImpExLogFilter.LocationProvider
{
    protected static final Logger LOG = Logger.getLogger(ImpExWorker.class.getName());
    private final MultiThreadedImpExImportReader reader;
    private final Tenant tenant;
    private final JaloSession jaloSession;
    private final CronJob.CronJobThreadSettings cjThreadSettings;
    private final int number;
    private final long createdTimestamp;
    private final MediaDataHandler mdh;
    private final PoolableThread workerTread;
    static final int NOT_STARTED = 0;
    static final int STARTED = 1;
    static final int FINISHED = 2;
    static final int SKIP_START = 3;
    private final AtomicInteger _state = new AtomicInteger(0);
    private volatile ValueLine currentLine;


    ImpExWorker(PoolableThread poolableThread, MultiThreadedImpExImportReader reader, int number)
    {
        if(reader == null)
        {
            throw new IllegalStateException("Reader can not be null!");
        }
        this.reader = reader;
        this.number = number;
        this.jaloSession = JaloSession.getCurrentSession();
        this.cjThreadSettings = CronJob.getCronJobThreadSettings();
        this.tenant = this.jaloSession.getTenant();
        this.mdh = MediaDataTranslator.hasHandler() ? MediaDataTranslator.getHandler() : null;
        this.workerTread = poolableThread;
        this.createdTimestamp = System.currentTimeMillis();
    }


    int getNumber()
    {
        return this.number;
    }


    String getCronJobCodeIfAvailable()
    {
        return this.cjThreadSettings.getCronJobCodeIfAvailable();
    }


    public void run()
    {
        try
        {
            prepareThread();
            if(setStarted())
            {
                perform();
            }
        }
        catch(Throwable e)
        {
            LOG.error("unexpected error in " + this + " : " + e.getMessage(), e);
        }
        finally
        {
            LOG.info("Returning worker " + this + " to the pool");
            setFinished();
            cleanupThread();
        }
    }


    public String toString()
    {
        String cronJobCode = getCronJobCodeIfAvailable();
        StringBuilder sb = new StringBuilder();
        sb.append("impex worker ").append(getNumber()).append('/').append(getReader().getMaxThreads());
        sb.append(" [");
        if(cronJobCode != null)
        {
            sb.append("cj:").append(cronJobCode);
        }
        else
        {
            sb.append("hash:").append(System.identityHashCode(this));
        }
        sb.append(']');
        return sb.toString();
    }


    protected void prepareThread()
    {
        Registry.setCurrentTenant(this.tenant);
        this.jaloSession.activate();
        CronJob.activateCronJobThreadSettings(this.cjThreadSettings);
        ImpExLogFilter filter = getReader().getLogFilter();
        if(filter != null)
        {
            filter.registerLocationProvider(this);
        }
        if(this.mdh != null)
        {
            MediaDataTranslator.setMediaDataHandler(this.mdh);
        }
        else
        {
            MediaDataTranslator.unsetMediaDataHandler();
        }
    }


    protected void cleanupThread()
    {
        ImpExLogFilter filter = getReader().getLogFilter();
        if(filter != null)
        {
            filter.unregisterLocationProvider();
        }
        MediaDataTranslator.unsetMediaDataHandler();
        CronJob.unsetCronJobThreadSettings(this.cjThreadSettings);
        JaloSession.deactivate();
        Registry.unsetCurrentTenant();
    }


    protected void perform()
    {
        MultiThreadedImpExImportReader impExImportReader = getReader();
        for(this.currentLine = impExImportReader.fetchNextValueLine(this); this.currentLine != null; this
                        .currentLine = impExImportReader.fetchNextValueLine(this))
        {
            Item ret = null;
            Exception error = null;
            try
            {
                ret = impExImportReader.processValueLineFromWorker(this.currentLine);
            }
            catch(Exception e)
            {
                error = e;
            }
            impExImportReader.addResult(new ImpExWorkerResult(this, this.currentLine, ret, error));
        }
    }


    protected MultiThreadedImpExImportReader getReader()
    {
        return this.reader;
    }


    protected long getCreationTimestamp()
    {
        return this.createdTimestamp;
    }


    protected boolean isFinished()
    {
        int state = this._state.get();
        return (state == 2 || state == 3);
    }


    protected boolean isStarted()
    {
        return (this._state.get() != 0);
    }


    private boolean setStarted()
    {
        return this._state.compareAndSet(0, 1);
    }


    private void setFinished()
    {
        this._state.set(2);
    }


    boolean checkFinishedOrSkipStart()
    {
        if(isFinished())
        {
            return true;
        }
        if(this._state.compareAndSet(0, 3))
        {
            LOG.info("worker " + this + " has not started yet and will skip work completely");
            return true;
        }
        return isFinished();
    }


    protected void start()
    {
        getWorkerTread().execute(this);
    }


    protected PoolableThread getWorkerTread()
    {
        return this.workerTread;
    }


    public String getProcessName()
    {
        return "ImpExWorker<" + getNumber() + "/" + getReader().getMaxThreads() + ">";
    }


    public String getCurrentLocation()
    {
        ValueLine line = this.currentLine;
        if(line == null)
        {
            return "";
        }
        return line.getLocation();
    }
}

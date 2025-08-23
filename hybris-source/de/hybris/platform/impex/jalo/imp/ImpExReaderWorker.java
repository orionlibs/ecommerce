package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.util.threadpool.PoolableThread;

public class ImpExReaderWorker extends ImpExWorker
{
    protected ImpExReaderWorker(PoolableThread poolableThread, MultiThreadedImpExImportReader reader)
    {
        super(poolableThread, reader, -1);
    }


    protected void perform()
    {
        try
        {
            boolean moreLines = true;
            do
            {
                try
                {
                    moreLines = getReader().readLineFromWorker();
                }
                catch(ImpExException e)
                {
                    LOG.error("error while reading lines", (Throwable)e);
                    getReader().addReadingError(e, this);
                }
            }
            while(moreLines);
        }
        catch(Exception e)
        {
            LOG.error("unexpected error reading : " + e.getMessage(), e);
        }
    }


    public String getProcessName()
    {
        return toString();
    }


    public String toString()
    {
        String cronJobCode = getCronJobCodeIfAvailable();
        StringBuilder sb = new StringBuilder();
        sb.append("impex reader worker");
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


    public String getCurrentLocation()
    {
        MultiThreadedImpExImportReader multiThreadedImpExImportReader = getReader();
        if(multiThreadedImpExImportReader == null)
        {
            return null;
        }
        return multiThreadedImpExImportReader.getCurrentLocation();
    }
}

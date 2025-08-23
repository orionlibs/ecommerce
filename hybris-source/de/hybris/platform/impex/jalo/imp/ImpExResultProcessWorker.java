package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.util.threadpool.PoolableThread;

public class ImpExResultProcessWorker extends ImpExWorker
{
    private volatile ImpExWorkerResult currentResult;


    protected ImpExResultProcessWorker(PoolableThread poolableThread, MultiThreadedImpExImportReader reader)
    {
        super(poolableThread, reader, -1);
    }


    protected void perform()
    {
        this.currentResult = getReader().fetchNextWorkerResult();
        while(this.currentResult != null)
        {
            getReader().processPendingResult(this.currentResult);
            this.currentResult = getReader().fetchNextWorkerResult();
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
        sb.append("impex result worker");
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
        ImpExWorkerResult result = this.currentResult;
        if(result == null || result.getLine() == null)
        {
            return "";
        }
        return result.getLine().getLocation();
    }
}

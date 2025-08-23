package de.hybris.platform.cluster;

import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.Logger;

public class ClusterNodePingUpdater extends RegistrableThread
{
    private static final Logger LOG = Logger.getLogger(ClusterNodePingUpdater.class.getName());
    private final int ownClusterID;
    private final ClusterNodeDAO dao;
    private final long interval;
    private final AtomicBoolean stopped = new AtomicBoolean(false);


    public ClusterNodePingUpdater(ClusterNodeDAO dao, int ownClusterID, long interval)
    {
        this.ownClusterID = ownClusterID;
        this.interval = interval;
        this.dao = dao;
        withInitialInfo();
    }


    protected void withInitialInfo()
    {
        withInitialInfo(OperationInfo.builder().asSuspendableOperation(true).build());
    }


    public void internalRun()
    {
        Thread.currentThread().setName("ClusterNodePingUpdater<" + this.ownClusterID + ">");
        do
        {
            try
            {
                Thread.sleep(this.interval);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                break;
            }
            if(this.stopped.get())
            {
                continue;
            }
            updateNodeRecord();
        }
        while(!this.stopped.get());
    }


    protected void updateNodeRecord()
    {
        try
        {
            LOG.debug("Updating cluster node record for " + this.ownClusterID + " with new ping time");
            this.dao.ping(this.ownClusterID);
        }
        catch(Exception e)
        {
            LOG.error("error pining own cluster node record for id " + this.ownClusterID, e);
        }
    }


    public void stopUpdatingAndFinish(long timeoutMillis)
    {
        this.stopped.set(true);
        try
        {
            join(timeoutMillis);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}

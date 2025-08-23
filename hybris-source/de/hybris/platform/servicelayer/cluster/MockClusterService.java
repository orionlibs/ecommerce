package de.hybris.platform.servicelayer.cluster;

import de.hybris.platform.servicelayer.internal.service.AbstractService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;

public class MockClusterService extends AbstractService implements ClusterService
{
    private int clusterId;
    private long clusterIslandId;
    private Collection<String> nodeGroups;


    public int getClusterId()
    {
        return this.clusterId;
    }


    @Required
    public void setClusterId(int clusterId)
    {
        this.clusterId = clusterId;
    }


    public long getClusterIslandId()
    {
        return this.clusterIslandId;
    }


    @Required
    public void setClusterIslandId(long clusterIslandId)
    {
        this.clusterIslandId = clusterIslandId;
    }


    public boolean isClusteringEnabled()
    {
        return false;
    }


    public void setClusterGroups(Collection<String> nodeGroups)
    {
        this.nodeGroups = (nodeGroups == null) ? null : new ArrayList<>(nodeGroups);
    }


    public Collection<String> getClusterGroups()
    {
        return (this.nodeGroups == null) ? Collections.<String>emptyList() : new ArrayList<>(this.nodeGroups);
    }
}

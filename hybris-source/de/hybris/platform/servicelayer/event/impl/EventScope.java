package de.hybris.platform.servicelayer.event.impl;

import java.io.Serializable;

public class EventScope implements Serializable
{
    private String tenantId;
    private int clusterId;
    private long clusterIslandId;


    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public void setClusterId(int clusterId)
    {
        this.clusterId = clusterId;
    }


    public int getClusterId()
    {
        return this.clusterId;
    }


    public void setClusterIslandId(long clusterIslandId)
    {
        this.clusterIslandId = clusterIslandId;
    }


    public long getClusterIslandId()
    {
        return this.clusterIslandId;
    }


    public String toString()
    {
        return "<clusterIslandId=" + this.clusterIslandId + ",clusterId=" + this.clusterId + "/tenantId=" + this.tenantId + ">";
    }
}

package de.hybris.platform.hac.data.dto.cluster;

import java.util.List;

public class ClusterData
{
    private boolean clusterEnabled;
    private long clusterIslandId;
    private long clusterNodeId;
    private long dynamicClusterNodeId;
    private List<NodeData> nodes;
    private List<BroadcastMethodData> broadcastMethods;


    public boolean isClusterEnabled()
    {
        return this.clusterEnabled;
    }


    public void setClusterEnabled(boolean clusterEnabled)
    {
        this.clusterEnabled = clusterEnabled;
    }


    public long getClusterIslandId()
    {
        return this.clusterIslandId;
    }


    public void setClusterIslandId(long clusterIslandId)
    {
        this.clusterIslandId = clusterIslandId;
    }


    public long getClusterNodeId()
    {
        return this.clusterNodeId;
    }


    public void setClusterNodeId(long clusterNodeId)
    {
        this.clusterNodeId = clusterNodeId;
    }


    public long getDynamicClusterNodeId()
    {
        return this.dynamicClusterNodeId;
    }


    public void setDynamicClusterNodeId(long dynamicClusterNodeId)
    {
        this.dynamicClusterNodeId = dynamicClusterNodeId;
    }


    public List<NodeData> getNodes()
    {
        return this.nodes;
    }


    public void setNodes(List<NodeData> nodes)
    {
        this.nodes = nodes;
    }


    public List<BroadcastMethodData> getBroadcastMethods()
    {
        return this.broadcastMethods;
    }


    public void setBroadcastMethods(List<BroadcastMethodData> clusterNodes)
    {
        this.broadcastMethods = clusterNodes;
    }
}

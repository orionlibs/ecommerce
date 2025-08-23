package de.hybris.platform.cluster;

public class ClusterNodeAcquisition
{
    private final ClusterNodeInfo node;
    private final NodeState startupState;


    public ClusterNodeAcquisition(ClusterNodeInfo node, NodeState state)
    {
        this.node = node;
        this.startupState = state;
    }


    public ClusterNodeInfo getNode()
    {
        return this.node;
    }


    public NodeState getStartupState()
    {
        return this.startupState;
    }
}

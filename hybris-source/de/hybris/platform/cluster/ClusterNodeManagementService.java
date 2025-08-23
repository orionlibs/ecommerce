package de.hybris.platform.cluster;

import java.util.Collection;
import java.util.Collections;

public interface ClusterNodeManagementService
{
    int getClusterID();


    boolean isAutoDiscoveryEnabled();


    ClusterNodeAcquisition.NodeState getNodeStartupState();


    Collection<String> getClusterGroups();


    long getUpdateInterval();


    long getStaleNodeTimeout();


    int getConfiguredClusterID();


    default Collection<ClusterNodeInfo> findAllNodes()
    {
        return Collections.emptyList();
    }
}

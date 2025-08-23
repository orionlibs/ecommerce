package de.hybris.platform.cluster;

import java.util.Collection;

public interface ClusterNodeDAO
{
    void initializePersistence();


    Collection<ClusterNodeInfo> findAll();


    Collection<ClusterNodeInfo> findStaleNodes();


    ClusterNodeInfo get(int paramInt);


    boolean remove(int paramInt);


    ClusterNodeAcquisition acquireNodeID(ClusterNodeInfo paramClusterNodeInfo);


    void ping(int paramInt);
}

package de.hybris.platform.servicelayer.cluster;

import java.util.Collection;

public interface ClusterService
{
    int getClusterId();


    long getClusterIslandId();


    boolean isClusteringEnabled();


    Collection<String> getClusterGroups();
}

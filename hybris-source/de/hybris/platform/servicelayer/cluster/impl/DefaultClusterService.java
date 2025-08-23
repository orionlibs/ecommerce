package de.hybris.platform.servicelayer.cluster.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import java.util.Collection;

public class DefaultClusterService extends AbstractService implements ClusterService
{
    public int getClusterId()
    {
        return Registry.getClusterID();
    }


    public long getClusterIslandId()
    {
        return Registry.getMasterTenant().getClusterIslandPK();
    }


    public Collection<String> getClusterGroups()
    {
        return Registry.getClusterGroups();
    }


    public boolean isClusteringEnabled()
    {
        return Registry.getMasterTenant().isClusteringEnabled();
    }
}

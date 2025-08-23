package de.hybris.platform.cluster;

import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.DataSourceFactory;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.config.ConfigIntf;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultClusterNodeManagementService implements ClusterNodeManagementService
{
    private static final Logger LOG = Logger.getLogger(DefaultClusterNodeManagementService.class.getName());


    public static ClusterNodeManagementService getInstance()
    {
        return (ClusterNodeManagementService)Registry.getNonTenantSingleton(SINGLETON_CREATOR);
    }


    private static final ClusterNodeAutoDiscoverySettings NO_AUTO_DISCOVERY = new ClusterNodeAutoDiscoverySettings(null, null, null, null);
    private final ConfigIntf masterTenantConfig;
    private final ClusterNodeAutoDiscoverySettings currentDiscoverySettings;
    private final Collection<String> clusterGroups;


    DefaultClusterNodeManagementService(ConfigIntf masterTenantConfig)
    {
        this.masterTenantConfig = masterTenantConfig;
        Set<String> groups = new LinkedHashSet<>();
        String groupsCfg = masterTenantConfig.getParameter("cluster.node.groups");
        if(StringUtils.isNotBlank(groupsCfg))
        {
            for(String grp : groupsCfg.split(","))
            {
                if(StringUtils.isNotBlank(grp))
                {
                    groups.add(grp.trim());
                }
            }
        }
        this.clusterGroups = Collections.unmodifiableSet(groups);
        this.currentDiscoverySettings = initialize(masterTenantConfig);
    }


    private ClusterNodeAutoDiscoverySettings initialize(ConfigIntf masterTenantConfig)
    {
        if(isAutoDiscoveryEnabled())
        {
            ClusterNodeAutoDiscoverySettings settings = startClusterNodeAutoDiscovery(createDataSource(masterTenantConfig,
                            createDataSourceFactory()));
            LOG.info("Automatic cluster ID discovery enabled. Acquired ID: " + getConfiguredClusterID());
            return settings;
        }
        LOG.info("Automatic cluster ID discovery disabled. Configured ID: " + getConfiguredClusterID());
        return NO_AUTO_DISCOVERY;
    }


    public int getClusterID()
    {
        return (this.currentDiscoverySettings.node == null) ? getConfiguredClusterID() : this.currentDiscoverySettings.node.getId();
    }


    public Collection<String> getClusterGroups()
    {
        return this.clusterGroups;
    }


    public ClusterNodeAcquisition.NodeState getNodeStartupState()
    {
        return this.currentDiscoverySettings.startupState;
    }


    public boolean isAutoDiscoveryEnabled()
    {
        return this.masterTenantConfig.getBoolean("cluster.nodes.autodiscovery", false);
    }


    public Collection<ClusterNodeInfo> findAllNodes()
    {
        return (this.currentDiscoverySettings.clusterNodeDAO != null) ? this.currentDiscoverySettings.clusterNodeDAO.findAll() : Collections.<ClusterNodeInfo>emptyList();
    }


    private HybrisDataSource createDataSource(ConfigIntf cfg, DataSourceFactory factory)
    {
        String fromJNDI = cfg.getString(Config.SystemSpecificParams.DB_POOL_FROMJNDI, null);
        if(fromJNDI != null)
        {
            return factory.createJNDIDataSource("master", (Tenant)Registry.getMasterTenant(), fromJNDI, false);
        }
        Map<String, String> params = new HashMap<>(5);
        params.put(Config.SystemSpecificParams.DB_USERNAME, cfg.getParameter(Config.SystemSpecificParams.DB_USERNAME));
        params.put(Config.SystemSpecificParams.DB_PASSWORD, cfg.getParameter(Config.SystemSpecificParams.DB_PASSWORD));
        params.put(Config.SystemSpecificParams.DB_URL, cfg.getParameter(Config.SystemSpecificParams.DB_URL));
        params.put(Config.SystemSpecificParams.DB_DRIVER, cfg.getParameter(Config.SystemSpecificParams.DB_DRIVER));
        params.put(Config.SystemSpecificParams.DB_TABLEPREFIX, cfg.getParameter(Config.SystemSpecificParams.DB_TABLEPREFIX));
        params.put("db.customsessionsql", cfg.getParameter("db.customsessionsql"));
        Map<String, String> customParams = AbstractTenant.extractCustomDBParams(cfg);
        params.putAll(customParams);
        if(MapUtils.isNotEmpty(customParams) && LOG.isDebugEnabled())
        {
            LOG.debug("Using custom jdbc parameters for master data source : " + customParams + ".");
        }
        return factory.createDataSource("master", (Tenant)Registry.getMasterTenant(), params, false);
    }


    private DataSourceFactory createDataSourceFactory()
    {
        return (DataSourceFactory)new MyDataSourceFactory();
    }


    public int getConfiguredClusterID()
    {
        return this.masterTenantConfig.getInt(Config.Params.CLUSTER_ID, 0);
    }


    private ClusterNodeAutoDiscoverySettings startClusterNodeAutoDiscovery(HybrisDataSource masterDataSource)
    {
        long interval = getUpdateInterval();
        long timeout = getStaleNodeTimeout();
        DefaultClusterNodeDAO clusterNodeDAO = new DefaultClusterNodeDAO(masterDataSource, timeout);
        clusterNodeDAO.initializePersistence();
        ClusterNodeAcquisition assignedClusterNode = clusterNodeDAO.acquireNodeID(createOnwClusterNodeInfo(getConfiguredClusterID()));
        changeConfig(assignedClusterNode);
        logNodeAssignment(assignedClusterNode);
        ClusterNodePingUpdater clusterNodePingUpdater = new ClusterNodePingUpdater((ClusterNodeDAO)clusterNodeDAO, assignedClusterNode.getNode().getId(), interval);
        clusterNodePingUpdater.start();
        return new ClusterNodeAutoDiscoverySettings(masterDataSource, assignedClusterNode, (ClusterNodeDAO)clusterNodeDAO, clusterNodePingUpdater);
    }


    public long getStaleNodeTimeout()
    {
        return this.masterTenantConfig.getLong("cluster.nodes.stale.timeout", 30000L);
    }


    public long getUpdateInterval()
    {
        return this.masterTenantConfig.getLong("cluster.nodes.ping.interval", 10000L);
    }


    private boolean isUnregisterOnShutdown()
    {
        return this.masterTenantConfig.getBoolean("cluster.nodes.unregisterOnShutdown", true);
    }


    protected void releaseAutomaticClusterID()
    {
        if(this.currentDiscoverySettings != null)
        {
            LOG.info("Unregistering assigned cluster node id " + getClusterID() + "...");
            if(this.currentDiscoverySettings.clusterNodePingUpdater != null)
            {
                this.currentDiscoverySettings.clusterNodePingUpdater.stopUpdatingAndFinish(2L * getUpdateInterval());
            }
            if(this.currentDiscoverySettings.clusterNodeDAO != null && isUnregisterOnShutdown())
            {
                this.currentDiscoverySettings.clusterNodeDAO.remove(getClusterID());
            }
            if(this.currentDiscoverySettings.dataSource != null)
            {
                this.currentDiscoverySettings.dataSource.destroy();
            }
        }
    }


    private void changeConfig(ClusterNodeAcquisition assignment)
    {
        this.masterTenantConfig.setParameter(Config.Params.CLUSTER_ID, Integer.toString(assignment.getNode().getId()));
    }


    private void logNodeAssignment(ClusterNodeAcquisition nodeAssignmet)
    {
        LOG.info("Assigned to cluster node " + nodeAssignmet.getNode() + " (state:" + nodeAssignmet.getStartupState() + ")");
    }


    private ClusterNodeInfo createOnwClusterNodeInfo(int clusterIDFomConfig)
    {
        String ip = "IP:<n/a>";
        try
        {
            ip = "IP:" + InetAddress.getLocalHost().getHostAddress();
        }
        catch(UnknownHostException e)
        {
            LOG.debug(e.getMessage(), e);
        }
        return new ClusterNodeInfo(clusterIDFomConfig, ip);
    }


    private static final SingletonCreator.Creator<ClusterNodeManagementService> SINGLETON_CREATOR = (SingletonCreator.Creator<ClusterNodeManagementService>)new Object();
}

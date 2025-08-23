package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.SolrQueryMethod;
import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import java.util.List;

public class SolrServerConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SolrServerConfig";
    public static final String NAME = "name";
    public static final String MODE = "mode";
    public static final String EMBEDDEDMASTER = "embeddedMaster";
    public static final String ALIVECHECKINTERVAL = "aliveCheckInterval";
    public static final String CONNECTIONTIMEOUT = "connectionTimeout";
    public static final String SOCKETTIMEOUT = "socketTimeout";
    public static final String TCPNODELAY = "tcpNoDelay";
    public static final String MAXTOTALCONNECTIONS = "maxTotalConnections";
    public static final String MAXTOTALCONNECTIONSPERHOSTCONFIG = "maxTotalConnectionsPerHostConfig";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String INDEXINGALIVECHECKINTERVAL = "indexingAliveCheckInterval";
    public static final String INDEXINGCONNECTIONTIMEOUT = "indexingConnectionTimeout";
    public static final String INDEXINGSOCKETTIMEOUT = "indexingSocketTimeout";
    public static final String INDEXINGTCPNODELAY = "indexingTcpNoDelay";
    public static final String INDEXINGMAXTOTALCONNECTIONS = "indexingMaxTotalConnections";
    public static final String INDEXINGMAXTOTALCONNECTIONSPERHOSTCONFIG = "indexingMaxTotalConnectionsPerHostConfig";
    public static final String INDEXINGUSERNAME = "indexingUsername";
    public static final String INDEXINGPASSWORD = "indexingPassword";
    public static final String READTIMEOUT = "readTimeout";
    public static final String USEMASTERNODEEXCLUSIVELYFORINDEXING = "useMasterNodeExclusivelyForIndexing";
    public static final String NUMSHARDS = "numShards";
    public static final String REPLICATIONFACTOR = "replicationFactor";
    public static final String AUTOADDREPLICAS = "autoAddReplicas";
    public static final String SOLRQUERYMETHOD = "solrQueryMethod";
    public static final String VERSION = "version";
    public static final String SOLRENDPOINTURLS = "solrEndpointUrls";


    public SolrServerConfigModel()
    {
    }


    public SolrServerConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrServerConfigModel(SolrServerModes _mode, String _name, boolean _useMasterNodeExclusivelyForIndexing)
    {
        setMode(_mode);
        setName(_name);
        setUseMasterNodeExclusivelyForIndexing(_useMasterNodeExclusivelyForIndexing);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrServerConfigModel(SolrServerModes _mode, String _name, ItemModel _owner, boolean _useMasterNodeExclusivelyForIndexing)
    {
        setMode(_mode);
        setName(_name);
        setOwner(_owner);
        setUseMasterNodeExclusivelyForIndexing(_useMasterNodeExclusivelyForIndexing);
    }


    @Accessor(qualifier = "aliveCheckInterval", type = Accessor.Type.GETTER)
    public Integer getAliveCheckInterval()
    {
        return (Integer)getPersistenceContext().getPropertyValue("aliveCheckInterval");
    }


    @Accessor(qualifier = "connectionTimeout", type = Accessor.Type.GETTER)
    public Integer getConnectionTimeout()
    {
        return (Integer)getPersistenceContext().getPropertyValue("connectionTimeout");
    }


    @Accessor(qualifier = "indexingAliveCheckInterval", type = Accessor.Type.GETTER)
    public Integer getIndexingAliveCheckInterval()
    {
        return (Integer)getPersistenceContext().getPropertyValue("indexingAliveCheckInterval");
    }


    @Accessor(qualifier = "indexingConnectionTimeout", type = Accessor.Type.GETTER)
    public Integer getIndexingConnectionTimeout()
    {
        return (Integer)getPersistenceContext().getPropertyValue("indexingConnectionTimeout");
    }


    @Accessor(qualifier = "indexingMaxTotalConnections", type = Accessor.Type.GETTER)
    public Integer getIndexingMaxTotalConnections()
    {
        return (Integer)getPersistenceContext().getPropertyValue("indexingMaxTotalConnections");
    }


    @Accessor(qualifier = "indexingMaxTotalConnectionsPerHostConfig", type = Accessor.Type.GETTER)
    public Integer getIndexingMaxTotalConnectionsPerHostConfig()
    {
        return (Integer)getPersistenceContext().getPropertyValue("indexingMaxTotalConnectionsPerHostConfig");
    }


    @Accessor(qualifier = "indexingPassword", type = Accessor.Type.GETTER)
    public String getIndexingPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("indexingPassword");
    }


    @Accessor(qualifier = "indexingSocketTimeout", type = Accessor.Type.GETTER)
    public Integer getIndexingSocketTimeout()
    {
        return (Integer)getPersistenceContext().getPropertyValue("indexingSocketTimeout");
    }


    @Accessor(qualifier = "indexingUsername", type = Accessor.Type.GETTER)
    public String getIndexingUsername()
    {
        return (String)getPersistenceContext().getPropertyValue("indexingUsername");
    }


    @Accessor(qualifier = "maxTotalConnections", type = Accessor.Type.GETTER)
    public Integer getMaxTotalConnections()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxTotalConnections");
    }


    @Accessor(qualifier = "maxTotalConnectionsPerHostConfig", type = Accessor.Type.GETTER)
    public Integer getMaxTotalConnectionsPerHostConfig()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxTotalConnectionsPerHostConfig");
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
    public SolrServerModes getMode()
    {
        return (SolrServerModes)getPersistenceContext().getPropertyValue("mode");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "numShards", type = Accessor.Type.GETTER)
    public Integer getNumShards()
    {
        return (Integer)getPersistenceContext().getPropertyValue("numShards");
    }


    @Accessor(qualifier = "password", type = Accessor.Type.GETTER)
    public String getPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("password");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "readTimeout", type = Accessor.Type.GETTER)
    public Integer getReadTimeout()
    {
        return (Integer)getPersistenceContext().getPropertyValue("readTimeout");
    }


    @Accessor(qualifier = "replicationFactor", type = Accessor.Type.GETTER)
    public Integer getReplicationFactor()
    {
        return (Integer)getPersistenceContext().getPropertyValue("replicationFactor");
    }


    @Accessor(qualifier = "socketTimeout", type = Accessor.Type.GETTER)
    public Integer getSocketTimeout()
    {
        return (Integer)getPersistenceContext().getPropertyValue("socketTimeout");
    }


    @Accessor(qualifier = "solrEndpointUrls", type = Accessor.Type.GETTER)
    public List<SolrEndpointUrlModel> getSolrEndpointUrls()
    {
        return (List<SolrEndpointUrlModel>)getPersistenceContext().getPropertyValue("solrEndpointUrls");
    }


    @Accessor(qualifier = "solrQueryMethod", type = Accessor.Type.GETTER)
    public SolrQueryMethod getSolrQueryMethod()
    {
        return (SolrQueryMethod)getPersistenceContext().getPropertyValue("solrQueryMethod");
    }


    @Accessor(qualifier = "username", type = Accessor.Type.GETTER)
    public String getUsername()
    {
        return (String)getPersistenceContext().getPropertyValue("username");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public String getVersion()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "version");
    }


    @Accessor(qualifier = "autoAddReplicas", type = Accessor.Type.GETTER)
    public boolean isAutoAddReplicas()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("autoAddReplicas"));
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "embeddedMaster", type = Accessor.Type.GETTER)
    public boolean isEmbeddedMaster()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("embeddedMaster"));
    }


    @Accessor(qualifier = "indexingTcpNoDelay", type = Accessor.Type.GETTER)
    public boolean isIndexingTcpNoDelay()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("indexingTcpNoDelay"));
    }


    @Accessor(qualifier = "tcpNoDelay", type = Accessor.Type.GETTER)
    public boolean isTcpNoDelay()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("tcpNoDelay"));
    }


    @Accessor(qualifier = "useMasterNodeExclusivelyForIndexing", type = Accessor.Type.GETTER)
    public boolean isUseMasterNodeExclusivelyForIndexing()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("useMasterNodeExclusivelyForIndexing"));
    }


    @Accessor(qualifier = "aliveCheckInterval", type = Accessor.Type.SETTER)
    public void setAliveCheckInterval(Integer value)
    {
        getPersistenceContext().setPropertyValue("aliveCheckInterval", value);
    }


    @Accessor(qualifier = "autoAddReplicas", type = Accessor.Type.SETTER)
    public void setAutoAddReplicas(boolean value)
    {
        getPersistenceContext().setPropertyValue("autoAddReplicas", toObject(value));
    }


    @Accessor(qualifier = "connectionTimeout", type = Accessor.Type.SETTER)
    public void setConnectionTimeout(Integer value)
    {
        getPersistenceContext().setPropertyValue("connectionTimeout", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "embeddedMaster", type = Accessor.Type.SETTER)
    public void setEmbeddedMaster(boolean value)
    {
        getPersistenceContext().setPropertyValue("embeddedMaster", toObject(value));
    }


    @Accessor(qualifier = "indexingAliveCheckInterval", type = Accessor.Type.SETTER)
    public void setIndexingAliveCheckInterval(Integer value)
    {
        getPersistenceContext().setPropertyValue("indexingAliveCheckInterval", value);
    }


    @Accessor(qualifier = "indexingConnectionTimeout", type = Accessor.Type.SETTER)
    public void setIndexingConnectionTimeout(Integer value)
    {
        getPersistenceContext().setPropertyValue("indexingConnectionTimeout", value);
    }


    @Accessor(qualifier = "indexingMaxTotalConnections", type = Accessor.Type.SETTER)
    public void setIndexingMaxTotalConnections(Integer value)
    {
        getPersistenceContext().setPropertyValue("indexingMaxTotalConnections", value);
    }


    @Accessor(qualifier = "indexingMaxTotalConnectionsPerHostConfig", type = Accessor.Type.SETTER)
    public void setIndexingMaxTotalConnectionsPerHostConfig(Integer value)
    {
        getPersistenceContext().setPropertyValue("indexingMaxTotalConnectionsPerHostConfig", value);
    }


    @Accessor(qualifier = "indexingPassword", type = Accessor.Type.SETTER)
    public void setIndexingPassword(String value)
    {
        getPersistenceContext().setPropertyValue("indexingPassword", value);
    }


    @Accessor(qualifier = "indexingSocketTimeout", type = Accessor.Type.SETTER)
    public void setIndexingSocketTimeout(Integer value)
    {
        getPersistenceContext().setPropertyValue("indexingSocketTimeout", value);
    }


    @Accessor(qualifier = "indexingTcpNoDelay", type = Accessor.Type.SETTER)
    public void setIndexingTcpNoDelay(boolean value)
    {
        getPersistenceContext().setPropertyValue("indexingTcpNoDelay", toObject(value));
    }


    @Accessor(qualifier = "indexingUsername", type = Accessor.Type.SETTER)
    public void setIndexingUsername(String value)
    {
        getPersistenceContext().setPropertyValue("indexingUsername", value);
    }


    @Accessor(qualifier = "maxTotalConnections", type = Accessor.Type.SETTER)
    public void setMaxTotalConnections(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxTotalConnections", value);
    }


    @Accessor(qualifier = "maxTotalConnectionsPerHostConfig", type = Accessor.Type.SETTER)
    public void setMaxTotalConnectionsPerHostConfig(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxTotalConnectionsPerHostConfig", value);
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
    public void setMode(SolrServerModes value)
    {
        getPersistenceContext().setPropertyValue("mode", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "numShards", type = Accessor.Type.SETTER)
    public void setNumShards(Integer value)
    {
        getPersistenceContext().setPropertyValue("numShards", value);
    }


    @Accessor(qualifier = "password", type = Accessor.Type.SETTER)
    public void setPassword(String value)
    {
        getPersistenceContext().setPropertyValue("password", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "readTimeout", type = Accessor.Type.SETTER)
    public void setReadTimeout(Integer value)
    {
        getPersistenceContext().setPropertyValue("readTimeout", value);
    }


    @Accessor(qualifier = "replicationFactor", type = Accessor.Type.SETTER)
    public void setReplicationFactor(Integer value)
    {
        getPersistenceContext().setPropertyValue("replicationFactor", value);
    }


    @Accessor(qualifier = "socketTimeout", type = Accessor.Type.SETTER)
    public void setSocketTimeout(Integer value)
    {
        getPersistenceContext().setPropertyValue("socketTimeout", value);
    }


    @Accessor(qualifier = "solrEndpointUrls", type = Accessor.Type.SETTER)
    public void setSolrEndpointUrls(List<SolrEndpointUrlModel> value)
    {
        getPersistenceContext().setPropertyValue("solrEndpointUrls", value);
    }


    @Accessor(qualifier = "solrQueryMethod", type = Accessor.Type.SETTER)
    public void setSolrQueryMethod(SolrQueryMethod value)
    {
        getPersistenceContext().setPropertyValue("solrQueryMethod", value);
    }


    @Accessor(qualifier = "tcpNoDelay", type = Accessor.Type.SETTER)
    public void setTcpNoDelay(boolean value)
    {
        getPersistenceContext().setPropertyValue("tcpNoDelay", toObject(value));
    }


    @Accessor(qualifier = "useMasterNodeExclusivelyForIndexing", type = Accessor.Type.SETTER)
    public void setUseMasterNodeExclusivelyForIndexing(boolean value)
    {
        getPersistenceContext().setPropertyValue("useMasterNodeExclusivelyForIndexing", toObject(value));
    }


    @Accessor(qualifier = "username", type = Accessor.Type.SETTER)
    public void setUsername(String value)
    {
        getPersistenceContext().setPropertyValue("username", value);
    }
}

package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrServerConfig extends GenericItem
{
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
    public static final String SOLRENDPOINTURLS = "solrEndpointUrls";
    protected static final OneToManyHandler<SolrEndpointUrl> SOLRENDPOINTURLSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRENDPOINTURL, false, "solrServerConfig", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("mode", Item.AttributeMode.INITIAL);
        tmp.put("embeddedMaster", Item.AttributeMode.INITIAL);
        tmp.put("aliveCheckInterval", Item.AttributeMode.INITIAL);
        tmp.put("connectionTimeout", Item.AttributeMode.INITIAL);
        tmp.put("socketTimeout", Item.AttributeMode.INITIAL);
        tmp.put("tcpNoDelay", Item.AttributeMode.INITIAL);
        tmp.put("maxTotalConnections", Item.AttributeMode.INITIAL);
        tmp.put("maxTotalConnectionsPerHostConfig", Item.AttributeMode.INITIAL);
        tmp.put("username", Item.AttributeMode.INITIAL);
        tmp.put("password", Item.AttributeMode.INITIAL);
        tmp.put("indexingAliveCheckInterval", Item.AttributeMode.INITIAL);
        tmp.put("indexingConnectionTimeout", Item.AttributeMode.INITIAL);
        tmp.put("indexingSocketTimeout", Item.AttributeMode.INITIAL);
        tmp.put("indexingTcpNoDelay", Item.AttributeMode.INITIAL);
        tmp.put("indexingMaxTotalConnections", Item.AttributeMode.INITIAL);
        tmp.put("indexingMaxTotalConnectionsPerHostConfig", Item.AttributeMode.INITIAL);
        tmp.put("indexingUsername", Item.AttributeMode.INITIAL);
        tmp.put("indexingPassword", Item.AttributeMode.INITIAL);
        tmp.put("readTimeout", Item.AttributeMode.INITIAL);
        tmp.put("useMasterNodeExclusivelyForIndexing", Item.AttributeMode.INITIAL);
        tmp.put("numShards", Item.AttributeMode.INITIAL);
        tmp.put("replicationFactor", Item.AttributeMode.INITIAL);
        tmp.put("autoAddReplicas", Item.AttributeMode.INITIAL);
        tmp.put("solrQueryMethod", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getAliveCheckInterval(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "aliveCheckInterval");
    }


    public Integer getAliveCheckInterval()
    {
        return getAliveCheckInterval(getSession().getSessionContext());
    }


    public int getAliveCheckIntervalAsPrimitive(SessionContext ctx)
    {
        Integer value = getAliveCheckInterval(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getAliveCheckIntervalAsPrimitive()
    {
        return getAliveCheckIntervalAsPrimitive(getSession().getSessionContext());
    }


    public void setAliveCheckInterval(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "aliveCheckInterval", value);
    }


    public void setAliveCheckInterval(Integer value)
    {
        setAliveCheckInterval(getSession().getSessionContext(), value);
    }


    public void setAliveCheckInterval(SessionContext ctx, int value)
    {
        setAliveCheckInterval(ctx, Integer.valueOf(value));
    }


    public void setAliveCheckInterval(int value)
    {
        setAliveCheckInterval(getSession().getSessionContext(), value);
    }


    public Boolean isAutoAddReplicas(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "autoAddReplicas");
    }


    public Boolean isAutoAddReplicas()
    {
        return isAutoAddReplicas(getSession().getSessionContext());
    }


    public boolean isAutoAddReplicasAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAutoAddReplicas(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAutoAddReplicasAsPrimitive()
    {
        return isAutoAddReplicasAsPrimitive(getSession().getSessionContext());
    }


    public void setAutoAddReplicas(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "autoAddReplicas", value);
    }


    public void setAutoAddReplicas(Boolean value)
    {
        setAutoAddReplicas(getSession().getSessionContext(), value);
    }


    public void setAutoAddReplicas(SessionContext ctx, boolean value)
    {
        setAutoAddReplicas(ctx, Boolean.valueOf(value));
    }


    public void setAutoAddReplicas(boolean value)
    {
        setAutoAddReplicas(getSession().getSessionContext(), value);
    }


    public Integer getConnectionTimeout(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "connectionTimeout");
    }


    public Integer getConnectionTimeout()
    {
        return getConnectionTimeout(getSession().getSessionContext());
    }


    public int getConnectionTimeoutAsPrimitive(SessionContext ctx)
    {
        Integer value = getConnectionTimeout(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getConnectionTimeoutAsPrimitive()
    {
        return getConnectionTimeoutAsPrimitive(getSession().getSessionContext());
    }


    public void setConnectionTimeout(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "connectionTimeout", value);
    }


    public void setConnectionTimeout(Integer value)
    {
        setConnectionTimeout(getSession().getSessionContext(), value);
    }


    public void setConnectionTimeout(SessionContext ctx, int value)
    {
        setConnectionTimeout(ctx, Integer.valueOf(value));
    }


    public void setConnectionTimeout(int value)
    {
        setConnectionTimeout(getSession().getSessionContext(), value);
    }


    public Boolean isEmbeddedMaster(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "embeddedMaster");
    }


    public Boolean isEmbeddedMaster()
    {
        return isEmbeddedMaster(getSession().getSessionContext());
    }


    public boolean isEmbeddedMasterAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEmbeddedMaster(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEmbeddedMasterAsPrimitive()
    {
        return isEmbeddedMasterAsPrimitive(getSession().getSessionContext());
    }


    public void setEmbeddedMaster(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "embeddedMaster", value);
    }


    public void setEmbeddedMaster(Boolean value)
    {
        setEmbeddedMaster(getSession().getSessionContext(), value);
    }


    public void setEmbeddedMaster(SessionContext ctx, boolean value)
    {
        setEmbeddedMaster(ctx, Boolean.valueOf(value));
    }


    public void setEmbeddedMaster(boolean value)
    {
        setEmbeddedMaster(getSession().getSessionContext(), value);
    }


    public Integer getIndexingAliveCheckInterval(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexingAliveCheckInterval");
    }


    public Integer getIndexingAliveCheckInterval()
    {
        return getIndexingAliveCheckInterval(getSession().getSessionContext());
    }


    public int getIndexingAliveCheckIntervalAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexingAliveCheckInterval(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getIndexingAliveCheckIntervalAsPrimitive()
    {
        return getIndexingAliveCheckIntervalAsPrimitive(getSession().getSessionContext());
    }


    public void setIndexingAliveCheckInterval(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexingAliveCheckInterval", value);
    }


    public void setIndexingAliveCheckInterval(Integer value)
    {
        setIndexingAliveCheckInterval(getSession().getSessionContext(), value);
    }


    public void setIndexingAliveCheckInterval(SessionContext ctx, int value)
    {
        setIndexingAliveCheckInterval(ctx, Integer.valueOf(value));
    }


    public void setIndexingAliveCheckInterval(int value)
    {
        setIndexingAliveCheckInterval(getSession().getSessionContext(), value);
    }


    public Integer getIndexingConnectionTimeout(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexingConnectionTimeout");
    }


    public Integer getIndexingConnectionTimeout()
    {
        return getIndexingConnectionTimeout(getSession().getSessionContext());
    }


    public int getIndexingConnectionTimeoutAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexingConnectionTimeout(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getIndexingConnectionTimeoutAsPrimitive()
    {
        return getIndexingConnectionTimeoutAsPrimitive(getSession().getSessionContext());
    }


    public void setIndexingConnectionTimeout(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexingConnectionTimeout", value);
    }


    public void setIndexingConnectionTimeout(Integer value)
    {
        setIndexingConnectionTimeout(getSession().getSessionContext(), value);
    }


    public void setIndexingConnectionTimeout(SessionContext ctx, int value)
    {
        setIndexingConnectionTimeout(ctx, Integer.valueOf(value));
    }


    public void setIndexingConnectionTimeout(int value)
    {
        setIndexingConnectionTimeout(getSession().getSessionContext(), value);
    }


    public Integer getIndexingMaxTotalConnections(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexingMaxTotalConnections");
    }


    public Integer getIndexingMaxTotalConnections()
    {
        return getIndexingMaxTotalConnections(getSession().getSessionContext());
    }


    public int getIndexingMaxTotalConnectionsAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexingMaxTotalConnections(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getIndexingMaxTotalConnectionsAsPrimitive()
    {
        return getIndexingMaxTotalConnectionsAsPrimitive(getSession().getSessionContext());
    }


    public void setIndexingMaxTotalConnections(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexingMaxTotalConnections", value);
    }


    public void setIndexingMaxTotalConnections(Integer value)
    {
        setIndexingMaxTotalConnections(getSession().getSessionContext(), value);
    }


    public void setIndexingMaxTotalConnections(SessionContext ctx, int value)
    {
        setIndexingMaxTotalConnections(ctx, Integer.valueOf(value));
    }


    public void setIndexingMaxTotalConnections(int value)
    {
        setIndexingMaxTotalConnections(getSession().getSessionContext(), value);
    }


    public Integer getIndexingMaxTotalConnectionsPerHostConfig(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexingMaxTotalConnectionsPerHostConfig");
    }


    public Integer getIndexingMaxTotalConnectionsPerHostConfig()
    {
        return getIndexingMaxTotalConnectionsPerHostConfig(getSession().getSessionContext());
    }


    public int getIndexingMaxTotalConnectionsPerHostConfigAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexingMaxTotalConnectionsPerHostConfig(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getIndexingMaxTotalConnectionsPerHostConfigAsPrimitive()
    {
        return getIndexingMaxTotalConnectionsPerHostConfigAsPrimitive(getSession().getSessionContext());
    }


    public void setIndexingMaxTotalConnectionsPerHostConfig(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexingMaxTotalConnectionsPerHostConfig", value);
    }


    public void setIndexingMaxTotalConnectionsPerHostConfig(Integer value)
    {
        setIndexingMaxTotalConnectionsPerHostConfig(getSession().getSessionContext(), value);
    }


    public void setIndexingMaxTotalConnectionsPerHostConfig(SessionContext ctx, int value)
    {
        setIndexingMaxTotalConnectionsPerHostConfig(ctx, Integer.valueOf(value));
    }


    public void setIndexingMaxTotalConnectionsPerHostConfig(int value)
    {
        setIndexingMaxTotalConnectionsPerHostConfig(getSession().getSessionContext(), value);
    }


    public String getIndexingPassword(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexingPassword");
    }


    public String getIndexingPassword()
    {
        return getIndexingPassword(getSession().getSessionContext());
    }


    public void setIndexingPassword(SessionContext ctx, String value)
    {
        setProperty(ctx, "indexingPassword", value);
    }


    public void setIndexingPassword(String value)
    {
        setIndexingPassword(getSession().getSessionContext(), value);
    }


    public Integer getIndexingSocketTimeout(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexingSocketTimeout");
    }


    public Integer getIndexingSocketTimeout()
    {
        return getIndexingSocketTimeout(getSession().getSessionContext());
    }


    public int getIndexingSocketTimeoutAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexingSocketTimeout(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getIndexingSocketTimeoutAsPrimitive()
    {
        return getIndexingSocketTimeoutAsPrimitive(getSession().getSessionContext());
    }


    public void setIndexingSocketTimeout(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexingSocketTimeout", value);
    }


    public void setIndexingSocketTimeout(Integer value)
    {
        setIndexingSocketTimeout(getSession().getSessionContext(), value);
    }


    public void setIndexingSocketTimeout(SessionContext ctx, int value)
    {
        setIndexingSocketTimeout(ctx, Integer.valueOf(value));
    }


    public void setIndexingSocketTimeout(int value)
    {
        setIndexingSocketTimeout(getSession().getSessionContext(), value);
    }


    public Boolean isIndexingTcpNoDelay(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "indexingTcpNoDelay");
    }


    public Boolean isIndexingTcpNoDelay()
    {
        return isIndexingTcpNoDelay(getSession().getSessionContext());
    }


    public boolean isIndexingTcpNoDelayAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIndexingTcpNoDelay(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIndexingTcpNoDelayAsPrimitive()
    {
        return isIndexingTcpNoDelayAsPrimitive(getSession().getSessionContext());
    }


    public void setIndexingTcpNoDelay(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "indexingTcpNoDelay", value);
    }


    public void setIndexingTcpNoDelay(Boolean value)
    {
        setIndexingTcpNoDelay(getSession().getSessionContext(), value);
    }


    public void setIndexingTcpNoDelay(SessionContext ctx, boolean value)
    {
        setIndexingTcpNoDelay(ctx, Boolean.valueOf(value));
    }


    public void setIndexingTcpNoDelay(boolean value)
    {
        setIndexingTcpNoDelay(getSession().getSessionContext(), value);
    }


    public String getIndexingUsername(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexingUsername");
    }


    public String getIndexingUsername()
    {
        return getIndexingUsername(getSession().getSessionContext());
    }


    public void setIndexingUsername(SessionContext ctx, String value)
    {
        setProperty(ctx, "indexingUsername", value);
    }


    public void setIndexingUsername(String value)
    {
        setIndexingUsername(getSession().getSessionContext(), value);
    }


    public Integer getMaxTotalConnections(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxTotalConnections");
    }


    public Integer getMaxTotalConnections()
    {
        return getMaxTotalConnections(getSession().getSessionContext());
    }


    public int getMaxTotalConnectionsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxTotalConnections(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxTotalConnectionsAsPrimitive()
    {
        return getMaxTotalConnectionsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxTotalConnections(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxTotalConnections", value);
    }


    public void setMaxTotalConnections(Integer value)
    {
        setMaxTotalConnections(getSession().getSessionContext(), value);
    }


    public void setMaxTotalConnections(SessionContext ctx, int value)
    {
        setMaxTotalConnections(ctx, Integer.valueOf(value));
    }


    public void setMaxTotalConnections(int value)
    {
        setMaxTotalConnections(getSession().getSessionContext(), value);
    }


    public Integer getMaxTotalConnectionsPerHostConfig(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxTotalConnectionsPerHostConfig");
    }


    public Integer getMaxTotalConnectionsPerHostConfig()
    {
        return getMaxTotalConnectionsPerHostConfig(getSession().getSessionContext());
    }


    public int getMaxTotalConnectionsPerHostConfigAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxTotalConnectionsPerHostConfig(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxTotalConnectionsPerHostConfigAsPrimitive()
    {
        return getMaxTotalConnectionsPerHostConfigAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxTotalConnectionsPerHostConfig(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxTotalConnectionsPerHostConfig", value);
    }


    public void setMaxTotalConnectionsPerHostConfig(Integer value)
    {
        setMaxTotalConnectionsPerHostConfig(getSession().getSessionContext(), value);
    }


    public void setMaxTotalConnectionsPerHostConfig(SessionContext ctx, int value)
    {
        setMaxTotalConnectionsPerHostConfig(ctx, Integer.valueOf(value));
    }


    public void setMaxTotalConnectionsPerHostConfig(int value)
    {
        setMaxTotalConnectionsPerHostConfig(getSession().getSessionContext(), value);
    }


    public EnumerationValue getMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "mode");
    }


    public EnumerationValue getMode()
    {
        return getMode(getSession().getSessionContext());
    }


    public void setMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "mode", value);
    }


    public void setMode(EnumerationValue value)
    {
        setMode(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public Integer getNumShards(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "numShards");
    }


    public Integer getNumShards()
    {
        return getNumShards(getSession().getSessionContext());
    }


    public int getNumShardsAsPrimitive(SessionContext ctx)
    {
        Integer value = getNumShards(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNumShardsAsPrimitive()
    {
        return getNumShardsAsPrimitive(getSession().getSessionContext());
    }


    public void setNumShards(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "numShards", value);
    }


    public void setNumShards(Integer value)
    {
        setNumShards(getSession().getSessionContext(), value);
    }


    public void setNumShards(SessionContext ctx, int value)
    {
        setNumShards(ctx, Integer.valueOf(value));
    }


    public void setNumShards(int value)
    {
        setNumShards(getSession().getSessionContext(), value);
    }


    public String getPassword(SessionContext ctx)
    {
        return (String)getProperty(ctx, "password");
    }


    public String getPassword()
    {
        return getPassword(getSession().getSessionContext());
    }


    public void setPassword(SessionContext ctx, String value)
    {
        setProperty(ctx, "password", value);
    }


    public void setPassword(String value)
    {
        setPassword(getSession().getSessionContext(), value);
    }


    public Integer getReadTimeout(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "readTimeout");
    }


    public Integer getReadTimeout()
    {
        return getReadTimeout(getSession().getSessionContext());
    }


    public int getReadTimeoutAsPrimitive(SessionContext ctx)
    {
        Integer value = getReadTimeout(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getReadTimeoutAsPrimitive()
    {
        return getReadTimeoutAsPrimitive(getSession().getSessionContext());
    }


    public void setReadTimeout(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "readTimeout", value);
    }


    public void setReadTimeout(Integer value)
    {
        setReadTimeout(getSession().getSessionContext(), value);
    }


    public void setReadTimeout(SessionContext ctx, int value)
    {
        setReadTimeout(ctx, Integer.valueOf(value));
    }


    public void setReadTimeout(int value)
    {
        setReadTimeout(getSession().getSessionContext(), value);
    }


    public Integer getReplicationFactor(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "replicationFactor");
    }


    public Integer getReplicationFactor()
    {
        return getReplicationFactor(getSession().getSessionContext());
    }


    public int getReplicationFactorAsPrimitive(SessionContext ctx)
    {
        Integer value = getReplicationFactor(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getReplicationFactorAsPrimitive()
    {
        return getReplicationFactorAsPrimitive(getSession().getSessionContext());
    }


    public void setReplicationFactor(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "replicationFactor", value);
    }


    public void setReplicationFactor(Integer value)
    {
        setReplicationFactor(getSession().getSessionContext(), value);
    }


    public void setReplicationFactor(SessionContext ctx, int value)
    {
        setReplicationFactor(ctx, Integer.valueOf(value));
    }


    public void setReplicationFactor(int value)
    {
        setReplicationFactor(getSession().getSessionContext(), value);
    }


    public Integer getSocketTimeout(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "socketTimeout");
    }


    public Integer getSocketTimeout()
    {
        return getSocketTimeout(getSession().getSessionContext());
    }


    public int getSocketTimeoutAsPrimitive(SessionContext ctx)
    {
        Integer value = getSocketTimeout(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getSocketTimeoutAsPrimitive()
    {
        return getSocketTimeoutAsPrimitive(getSession().getSessionContext());
    }


    public void setSocketTimeout(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "socketTimeout", value);
    }


    public void setSocketTimeout(Integer value)
    {
        setSocketTimeout(getSession().getSessionContext(), value);
    }


    public void setSocketTimeout(SessionContext ctx, int value)
    {
        setSocketTimeout(ctx, Integer.valueOf(value));
    }


    public void setSocketTimeout(int value)
    {
        setSocketTimeout(getSession().getSessionContext(), value);
    }


    public List<SolrEndpointUrl> getSolrEndpointUrls(SessionContext ctx)
    {
        return (List<SolrEndpointUrl>)SOLRENDPOINTURLSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrEndpointUrl> getSolrEndpointUrls()
    {
        return getSolrEndpointUrls(getSession().getSessionContext());
    }


    public void setSolrEndpointUrls(SessionContext ctx, List<SolrEndpointUrl> value)
    {
        SOLRENDPOINTURLSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrEndpointUrls(List<SolrEndpointUrl> value)
    {
        setSolrEndpointUrls(getSession().getSessionContext(), value);
    }


    public void addToSolrEndpointUrls(SessionContext ctx, SolrEndpointUrl value)
    {
        SOLRENDPOINTURLSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrEndpointUrls(SolrEndpointUrl value)
    {
        addToSolrEndpointUrls(getSession().getSessionContext(), value);
    }


    public void removeFromSolrEndpointUrls(SessionContext ctx, SolrEndpointUrl value)
    {
        SOLRENDPOINTURLSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrEndpointUrls(SolrEndpointUrl value)
    {
        removeFromSolrEndpointUrls(getSession().getSessionContext(), value);
    }


    public EnumerationValue getSolrQueryMethod(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "solrQueryMethod");
    }


    public EnumerationValue getSolrQueryMethod()
    {
        return getSolrQueryMethod(getSession().getSessionContext());
    }


    public void setSolrQueryMethod(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "solrQueryMethod", value);
    }


    public void setSolrQueryMethod(EnumerationValue value)
    {
        setSolrQueryMethod(getSession().getSessionContext(), value);
    }


    public Boolean isTcpNoDelay(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "tcpNoDelay");
    }


    public Boolean isTcpNoDelay()
    {
        return isTcpNoDelay(getSession().getSessionContext());
    }


    public boolean isTcpNoDelayAsPrimitive(SessionContext ctx)
    {
        Boolean value = isTcpNoDelay(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isTcpNoDelayAsPrimitive()
    {
        return isTcpNoDelayAsPrimitive(getSession().getSessionContext());
    }


    public void setTcpNoDelay(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "tcpNoDelay", value);
    }


    public void setTcpNoDelay(Boolean value)
    {
        setTcpNoDelay(getSession().getSessionContext(), value);
    }


    public void setTcpNoDelay(SessionContext ctx, boolean value)
    {
        setTcpNoDelay(ctx, Boolean.valueOf(value));
    }


    public void setTcpNoDelay(boolean value)
    {
        setTcpNoDelay(getSession().getSessionContext(), value);
    }


    public Boolean isUseMasterNodeExclusivelyForIndexing(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useMasterNodeExclusivelyForIndexing");
    }


    public Boolean isUseMasterNodeExclusivelyForIndexing()
    {
        return isUseMasterNodeExclusivelyForIndexing(getSession().getSessionContext());
    }


    public boolean isUseMasterNodeExclusivelyForIndexingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseMasterNodeExclusivelyForIndexing(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseMasterNodeExclusivelyForIndexingAsPrimitive()
    {
        return isUseMasterNodeExclusivelyForIndexingAsPrimitive(getSession().getSessionContext());
    }


    public void setUseMasterNodeExclusivelyForIndexing(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useMasterNodeExclusivelyForIndexing", value);
    }


    public void setUseMasterNodeExclusivelyForIndexing(Boolean value)
    {
        setUseMasterNodeExclusivelyForIndexing(getSession().getSessionContext(), value);
    }


    public void setUseMasterNodeExclusivelyForIndexing(SessionContext ctx, boolean value)
    {
        setUseMasterNodeExclusivelyForIndexing(ctx, Boolean.valueOf(value));
    }


    public void setUseMasterNodeExclusivelyForIndexing(boolean value)
    {
        setUseMasterNodeExclusivelyForIndexing(getSession().getSessionContext(), value);
    }


    public String getUsername(SessionContext ctx)
    {
        return (String)getProperty(ctx, "username");
    }


    public String getUsername()
    {
        return getUsername(getSession().getSessionContext());
    }


    public void setUsername(SessionContext ctx, String value)
    {
        setProperty(ctx, "username", value);
    }


    public void setUsername(String value)
    {
        setUsername(getSession().getSessionContext(), value);
    }
}

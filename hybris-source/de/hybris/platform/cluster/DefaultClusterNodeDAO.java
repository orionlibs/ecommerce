package de.hybris.platform.cluster;

import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DefaultClusterNodeDAO implements ClusterNodeDAO
{
    private static final Logger LOG = Logger.getLogger(DefaultClusterNodeDAO.class.getName());
    private static final String TABLE_BASE_NAME = "CLNodeInfos";
    private static final String COL_ID = "clusterID";
    private static final String COL_NAME = "nodeName";
    private static final String COL_CREATED = "createdTS";
    private static final String COL_LAST_PING = "lastPingTS";
    private final JdbcTemplate jdbcTemplate;
    private final long maxAllowedPingDelay;
    private final String tableName;
    private final String createTableSQL;


    public DefaultClusterNodeDAO(HybrisDataSource dataSource, long maxAllowedPingDelay)
    {
        this.jdbcTemplate = new JdbcTemplate((DataSource)dataSource);
        this.maxAllowedPingDelay = maxAllowedPingDelay;
        this.tableName = getTableName(dataSource);
        this.createTableSQL = getCreateTableSQL(dataSource);
    }


    protected String getTableName(HybrisDataSource hds)
    {
        String tablePrefix = hds.getTablePrefix();
        String tableName = tablePrefix + "CLNodeInfos";
        return tableName;
    }


    protected String getCreateTableSQL(HybrisDataSource hds)
    {
        String dbname = hds.getDatabaseName();
        if("mysql".equals(dbname))
        {
            String sql = "CREATE TABLE " + this.tableName + "(clusterID INTEGER NOT NULL, nodeName VARCHAR(255),createdTS BIGINT NOT NULL,lastPingTS BIGINT, CONSTRAINT PK_id_" + this.tableName + " PRIMARY KEY ( clusterID ) )";
            ConfigIntf cfg = hds.getTenant().getConfig();
            String tableType = cfg.getParameter("mysql.tabletype");
            if(StringUtils.isNotBlank(tableType))
            {
                sql = sql + " ENGINE  " + sql;
            }
            String optionalDefs = cfg.getParameter("mysql.optional.tabledefs");
            if(StringUtils.isNotBlank(optionalDefs))
            {
                sql = sql + " " + sql;
            }
            return sql;
        }
        if("sap".equals(dbname))
        {
            String sql = "CREATE TABLE " + this.tableName + "(clusterID INTEGER NOT NULL, nodeName NVARCHAR(255),createdTS BIGINT NOT NULL,lastPingTS BIGINT, PRIMARY KEY ( clusterID ) )";
            return sql;
        }
        if("oracle".equals(dbname))
        {
            String sql = "CREATE TABLE " + this.tableName + "(clusterID NUMBER(10) NOT NULL, nodeName VARCHAR2(255),createdTS NUMBER(20) NOT NULL,lastPingTS NUMBER(20), CONSTRAINT PK_id_" + this.tableName + " PRIMARY KEY ( clusterID ) )";
            return sql;
        }
        return "CREATE TABLE " + this.tableName + "(clusterID INTEGER NOT NULL, nodeName VARCHAR(255),createdTS BIGINT NOT NULL,lastPingTS BIGINT, CONSTRAINT PK_id_" + this.tableName + " PRIMARY KEY ( clusterID ) )";
    }


    public void initializePersistence()
    {
        try
        {
            get(0);
        }
        catch(Exception ignored)
        {
            try
            {
                this.jdbcTemplate.update(this.createTableSQL);
            }
            catch(DataAccessException ignored2)
            {
                try
                {
                    get(0);
                }
                catch(Exception eee)
                {
                    throw new SystemException("Couldn't initialite " + this.tableName + " ( node lookup and table creation failed )", eee);
                }
            }
        }
    }


    public Collection<ClusterNodeInfo> findAll()
    {
        try
        {
            return this.jdbcTemplate.query("SELECT clusterID, nodeName, createdTS, lastPingTS FROM " + this.tableName, (RowMapper)new Object(this));
        }
        catch(DataAccessException e)
        {
            throw new SystemException("error reading all cluster nodes", e);
        }
    }


    public Collection<ClusterNodeInfo> findStaleNodes()
    {
        long lastLegalPingTime = getLastLegalPingTS();
        try
        {
            return this.jdbcTemplate.query("SELECT clusterID, nodeName, createdTS, lastPingTS FROM " + this.tableName + " WHERE lastPingTS<?", new Object[] {Long.valueOf(lastLegalPingTime)}, (RowMapper)new Object(this));
        }
        catch(DataAccessException e)
        {
            throw new SystemException("error reading all cluster nodes", e);
        }
    }


    public ClusterNodeInfo get(int id)
    {
        try
        {
            List<ClusterNodeInfo> list = this.jdbcTemplate.query("SELECT nodeName, createdTS, lastPingTS FROM " + this.tableName + " WHERE clusterID=?", new Object[] {Integer.valueOf(id)}, (RowMapper)new Object(this, id));
            if(list.size() == 1)
            {
                return list.get(0);
            }
            if(list.isEmpty())
            {
                return null;
            }
            throw new IllegalStateException("More than one node record found for id " + id + " ( found " + list + ")");
        }
        catch(DataAccessException e)
        {
            throw new SystemException("error reading cluster node info with id " + id, e);
        }
    }


    public boolean remove(int id)
    {
        try
        {
            int updated = this.jdbcTemplate.update("DELETE FROM " + this.tableName + " WHERE clusterID=?", new Object[] {Integer.valueOf(id)});
            return (updated == 1);
        }
        catch(DataAccessException e)
        {
            throw new SystemException("error removing cluster id " + id, e);
        }
    }


    public void ping(int id)
    {
        try
        {
            int updated = this.jdbcTemplate.update("UPDATE " + this.tableName + " SET lastPingTS=? WHERE clusterID=?", new Object[] {Long.valueOf(System.currentTimeMillis()), Integer.valueOf(id)});
            if(updated == 0)
            {
                throw new IllegalStateException("Couldn't find cluster node record for id " + id + " for pinging it!");
            }
            if(updated > 1)
            {
                throw new IllegalStateException("More than one cluster node record for id " + id + " was updated during ping!");
            }
        }
        catch(DataAccessException e)
        {
            throw new SystemException("error removing cluster id " + id, e);
        }
    }


    public ClusterNodeAcquisition acquireNodeID(ClusterNodeInfo newInfoTemplate)
    {
        boolean success = false;
        int newID = -1;
        ClusterNodeAcquisition.NodeState nodeState = null;
        ClusterNodeInfo toRestart = null;
        long restartPingTS = -1L;
        while(!success)
        {
            Collection<ClusterNodeInfo> all = findAll();
            toRestart = getNextStaleNodeIDToReUse(all);
            if(toRestart != null)
            {
                LOG.debug("trying to restart existing cluster node " + toRestart);
                nodeState = ClusterNodeAcquisition.NodeState.RESTART_AFTER_CRASH;
                success = reset(toRestart, newInfoTemplate, restartPingTS = System.currentTimeMillis());
            }
            LOG.debug("trying to get new cluster ID");
            nodeState = ClusterNodeAcquisition.NodeState.NEW;
            newID = getNextUnusedID(all);
            success = insert(newID, newInfoTemplate);
        }
        if(nodeState == ClusterNodeAcquisition.NodeState.NEW)
        {
            return new ClusterNodeAcquisition(new ClusterNodeInfo(newID, newInfoTemplate
                            .getName(), newInfoTemplate.getCreatedTS(), newInfoTemplate
                            .getLastPingTS()), nodeState);
        }
        if(toRestart == null)
        {
            throw new IllegalStateException("toRestart must not be null");
        }
        return new ClusterNodeAcquisition(new ClusterNodeInfo(toRestart
                        .getId(), newInfoTemplate.getName(), toRestart.getCreatedTS(), restartPingTS), nodeState);
    }


    protected boolean reset(ClusterNodeInfo toReset, ClusterNodeInfo ownTemplate, long newPingTS)
    {
        try
        {
            int updated = this.jdbcTemplate.update("UPDATE " + this.tableName + " SET nodeName=? ,lastPingTS=? WHERE clusterID=? AND lastPingTS=?", new Object[] {ownTemplate
                            .getName(), Long.valueOf(newPingTS), Integer.valueOf(toReset.getId()),
                            Long.valueOf(toReset.getLastPingTS())});
            return (updated == 1);
        }
        catch(DataAccessException e)
        {
            throw new SystemException("error inserting cluster id " + toReset, e);
        }
    }


    protected boolean insert(int id, ClusterNodeInfo newInfoTemplate)
    {
        try
        {
            int inserted = this.jdbcTemplate.update("INSERT INTO " + this.tableName + " (clusterID, nodeName, createdTS, lastPingTS) VALUES (?,?,?,?)", new Object[] {Integer.valueOf(id), newInfoTemplate.getName(), Long.valueOf(newInfoTemplate.getCreatedTS()),
                            Long.valueOf(newInfoTemplate.getLastPingTS())});
            return (inserted == 1);
        }
        catch(DataIntegrityViolationException e)
        {
            LOG.debug("Cluster ID " + id + " cannot be inserted", (Throwable)e);
            return false;
        }
        catch(DataAccessException e)
        {
            throw new SystemException("error inserting cluster id " + id, e);
        }
    }


    protected ClusterNodeInfo getNextStaleNodeIDToReUse(Collection<ClusterNodeInfo> all)
    {
        long lastLegalPingTS = getLastLegalPingTS();
        for(ClusterNodeInfo clusterNodeInfo : all)
        {
            if(clusterNodeInfo.getLastPingTS() < lastLegalPingTS)
            {
                LOG.debug("found stale node " + clusterNodeInfo + " (lastLegalPing:" + lastLegalPingTS + ")");
                return clusterNodeInfo;
            }
        }
        return null;
    }


    protected long getLastLegalPingTS()
    {
        long lastLegalPingTS = System.currentTimeMillis() - this.maxAllowedPingDelay;
        return lastLegalPingTS;
    }


    protected int getNextUnusedID(Collection<ClusterNodeInfo> all)
    {
        int[] ids = new int[all.size()];
        int i = 0;
        for(ClusterNodeInfo clusterNodeInfo : all)
        {
            ids[i++] = clusterNodeInfo.getId();
        }
        Arrays.sort(ids);
        for(int candidate = 0; ; candidate++)
        {
            int arrayOfInt[] = ids, j = arrayOfInt.length;
            byte b = 0;
            while(true)
            {
                if(b < j)
                {
                    int existing = arrayOfInt[b];
                    if(existing == candidate)
                    {
                        break;
                    }
                    b++;
                    continue;
                }
                return candidate;
            }
        }
    }
}

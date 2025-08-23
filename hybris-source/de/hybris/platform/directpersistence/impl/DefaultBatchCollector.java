package de.hybris.platform.directpersistence.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.exception.UpdateRowException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class DefaultBatchCollector implements BatchCollector
{
    private static final Logger LOG = Logger.getLogger(DefaultBatchCollector.class);
    private final Map<String, BatchGroup> batchGroups = new LinkedHashMap<>();


    public void collectQuery(String sql, Object... params)
    {
        Preconditions.checkArgument((sql != null), "sql is required");
        Preconditions.checkArgument((params != null), "at least one param is required");
        collectQuery(sql, new BatchInfo(params));
    }


    public void collectQuery(String sql, Object[] params, BatchCollector.ResultCheck resultCheck)
    {
        Preconditions.checkArgument((sql != null), "sql is required");
        Preconditions.checkArgument((params != null), "params are required");
        collectQuery(sql, new BatchInfo(params, resultCheck));
    }


    public void collectQuery(String sql, PreparedStatementSetter statementSetter)
    {
        Preconditions.checkArgument((sql != null), "sql is required");
        Preconditions.checkArgument((statementSetter != null), "statementSetter is required");
        collectQuery(sql, new BatchInfo(statementSetter));
    }


    public void collectQuery(String sql, PreparedStatementSetter statementSetter, BatchCollector.ResultCheck resultCheck)
    {
        Preconditions.checkArgument((sql != null), "sql is required");
        Preconditions.checkArgument((statementSetter != null), "statementSetter is required");
        collectQuery(sql, new BatchInfo(statementSetter, resultCheck));
    }


    private void collectQuery(String sql, BatchInfo batchInfo)
    {
        Preconditions.checkArgument((sql != null), "sql is required");
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Registered BatchInfo [" + batchInfo + "]");
        }
        getBatchGroup(sql).addBatchInfo(batchInfo);
    }


    private BatchGroup getBatchGroup(String sql)
    {
        BatchGroup batchGroup = this.batchGroups.get(sql);
        if(batchGroup == null)
        {
            batchGroup = new BatchGroup();
            this.batchGroups.put(sql, batchGroup);
        }
        return batchGroup;
    }


    public Map<String, BatchGroup> getBatchGroups()
    {
        return (Map<String, BatchGroup>)ImmutableMap.copyOf(this.batchGroups);
    }


    public void batchUpdate(JdbcTemplate jdbcTemplate)
    {
        Map<String, BatchGroup> batchGroupsForProcess = preProcess(this.batchGroups);
        for(Map.Entry<String, BatchGroup> entry : batchGroupsForProcess.entrySet())
        {
            try
            {
                int[] result;
                BatchGroup batchGroup = entry.getValue();
                String sql = entry.getKey();
                if(batchGroup.isSettersBased())
                {
                    Object object = new Object(this, batchGroup);
                    result = decodeErrorCodesFromResult(jdbcTemplate.batchUpdate(sql, (BatchPreparedStatementSetter)object));
                }
                else
                {
                    result = decodeErrorCodesFromResult(jdbcTemplate.batchUpdate(sql, batchGroup.getParams()));
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("JDBC SQL Batch Update: " + (String)entry.getKey() + "\nbatchGroup: " + batchGroup + "\nresult " +
                                    Arrays.toString(result));
                }
                batchGroup.checkResult(result);
            }
            catch(UpdateRowException e)
            {
                LOG.error(e.getMessage(), (Throwable)e);
            }
        }
    }


    private int[] decodeErrorCodesFromResult(int[] result)
    {
        if(result == null)
        {
            return result;
        }
        return Arrays.stream(result).map(this::decodeErrorCode).toArray();
    }


    private int decodeErrorCode(int i)
    {
        if(i == -2)
        {
            return 1;
        }
        if(i == -3)
        {
            return 0;
        }
        return i;
    }


    protected Map<String, BatchGroup> preProcess(Map<String, BatchGroup> batchGroups)
    {
        return batchGroups;
    }
}

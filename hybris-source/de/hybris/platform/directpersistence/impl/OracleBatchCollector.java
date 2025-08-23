package de.hybris.platform.directpersistence.impl;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

public class OracleBatchCollector extends DefaultBatchCollector
{
    private static final Logger LOG = Logger.getLogger(OracleBatchCollector.class);
    private final JdbcTemplate jdbcTemplate;
    private final int batchSize;
    private final boolean optimisticLockEnabled;


    public OracleBatchCollector(JdbcTemplate jdbcTemplate, int batchSize, boolean optimisticLockEnabled)
    {
        Preconditions.checkArgument((jdbcTemplate != null));
        this.jdbcTemplate = jdbcTemplate;
        this.batchSize = batchSize;
        this.optimisticLockEnabled = optimisticLockEnabled;
    }


    protected Map<String, BatchGroup> preProcess(Map<String, BatchGroup> batchGroups)
    {
        for(Iterator<Map.Entry<String, BatchGroup>> it = batchGroups.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<String, BatchGroup> entry = it.next();
            BatchGroup batchGroup = entry.getValue();
            if(batchGroup.isCheckResult())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("JDBC SQL Batch Update: " + (String)entry.getKey() + "\nbatchGroup: " + batchGroup);
                }
                it.remove();
                if(batchGroup.getBatchInfos().size() == 1 || this.batchSize <= 1)
                {
                    updateSingle(entry, batchGroup);
                    continue;
                }
                updateBatch(entry.getKey(), batchGroup);
            }
        }
        return batchGroups;
    }


    private void updateSingle(Map.Entry<String, BatchGroup> entry, BatchGroup batchGroup)
    {
        int result;
        BatchInfo info = batchGroup.getBatchInfos().get(0);
        if(info.hasStatementSetter())
        {
            result = this.jdbcTemplate.update(entry.getKey(), info.getStatementSetter());
        }
        else
        {
            result = this.jdbcTemplate.update(entry.getKey(), info.getParams());
        }
        info.checkResult(result);
    }


    private void updateBatch(String sql, BatchGroup batchGroup)
    {
        this.jdbcTemplate.execute((ConnectionCallback)new BatchConnectionCallback(sql, batchGroup, this.batchSize, this.optimisticLockEnabled));
    }
}

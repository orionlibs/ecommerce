package de.hybris.platform.task.impl;

import com.google.common.base.Joiner;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.processengine.jalo.ProcessTask;
import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

public class TaskDAO
{
    private static final int NO_NODE_CLUSTER_ID = -1;
    private static final Logger LOG = Logger.getLogger(TaskDAO.class.getName());
    private final Tenant t;
    private final int clusterID;
    private final AbstractSynchronizedQueryCreator taskLockQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator taskUnLockQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator conditonMatchQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator conditonFulfillQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator conditonWithChoiceFulfillQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator conditionRemoveQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator conditonFetchQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator conditonConsumeQueryCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);
    private final AbstractSynchronizedQueryCreator allProcessTaskCacheCreator = (AbstractSynchronizedQueryCreator)new Object(this);


    public TaskDAO(Tenant t)
    {
        this.t = t;
        this.clusterID = initializeClusterId();
    }


    protected int initializeClusterId()
    {
        return MasterTenant.getInstance().getClusterID();
    }


    protected DataSource getDataSource()
    {
        return (DataSource)this.t.getMasterDataSource();
    }


    protected String getConditionFetchQuery()
    {
        return this.conditonFetchQueryCacheCreator.get();
    }


    protected String getConditionFulfillQuery()
    {
        return this.conditonFulfillQueryCacheCreator.get();
    }


    protected String getConditionWithChoiceFulfillQuery()
    {
        return this.conditonWithChoiceFulfillQueryCacheCreator.get();
    }


    private String getConditionRemoveQuery()
    {
        return this.conditionRemoveQueryCacheCreator.get();
    }


    protected String getConditionConsumeQuery()
    {
        return this.conditonConsumeQueryCacheCreator.get();
    }


    protected String getConditionMatchQuery()
    {
        return this.conditonMatchQueryCacheCreator.get();
    }


    protected String getLockQuery()
    {
        return this.taskLockQueryCacheCreator.get();
    }


    protected String getUnlockQuery()
    {
        return this.taskUnLockQueryCacheCreator.get();
    }


    protected String getAllProcessTaskQuery()
    {
        return this.allProcessTaskCacheCreator.get();
    }


    boolean lock(Long pk, long hjmpTS)
    {
        try
        {
            return lockWithTxRequired(pk, hjmpTS);
        }
        catch(DataAccessException e)
        {
            throw new IllegalStateException("Failed to lock task #" + pk + ".", e);
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Failed to lock task #" + pk + ".", e);
        }
    }


    private boolean lockWithTxRequired(Long pk, long hjmpTS) throws Exception
    {
        Transaction currentTx = Transaction.current();
        if(currentTx.isRunning())
        {
            return lockInternal(pk, hjmpTS);
        }
        Object txResult = currentTx.execute((TransactionBody)new Object(this, pk, hjmpTS));
        return Boolean.TRUE.equals(txResult);
    }


    private boolean lockInternal(Long pk, long hjmpTS)
    {
        int updates = update(
                        getLockQuery(), new Object[] {Integer.valueOf(this.clusterID),
                                        Integer.valueOf(-1), pk,
                                        Long.valueOf(hjmpTS)});
        if(updates > 0)
        {
            update(
                            getConditionConsumeQuery(), new Object[] {Boolean.TRUE, pk});
        }
        return (updates > 0);
    }


    void unlock(Long pk)
    {
        try
        {
            update(
                            getUnlockQuery(), new Object[] {Integer.valueOf(-1), pk});
        }
        catch(DataAccessException e)
        {
            throw new IllegalStateException("Failed to unlock task #" + pk + ".", e);
        }
    }


    boolean matchCondition(String uniqueID, PK taskPK)
    {
        try
        {
            int count = update(getConditionMatchQuery(), new Object[] {Long.valueOf(taskPK.getLongValue()), uniqueID});
            if(count > 0)
            {
                invalidateCache(uniqueID);
            }
            return (count > 0);
        }
        catch(DataAccessException e)
        {
            throw new IllegalStateException("Failed to match condition " + uniqueID + " for " + taskPK + ": " + e.getMessage(), e);
        }
    }


    boolean fulfillCondition(String uniqueID)
    {
        return fulfillCondition(TaskEvent.newEvent(uniqueID));
    }


    boolean fulfillCondition(TaskEvent event)
    {
        try
        {
            if(event.isFulfillmentByRemoval())
            {
                return removeCondition(event);
            }
            String query = getConditionFulfillQuery(event);
            Object[] params = getConditionFulfillQueryParams(event);
            int count = update(query, params);
            if(count > 0)
            {
                invalidateCache(event.getId());
            }
            return (count > 0);
        }
        catch(DataAccessException e)
        {
            throw new IllegalStateException("Failed to fulfill condition " + event + " : " + e.getMessage(), e);
        }
    }


    private boolean removeCondition(TaskEvent event)
    {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        List<Long> conditionPKs = jdbcTemplate.queryForList(getConditionFetchQuery(), Long.class, new Object[] {event.getId()});
        for(Long pk : conditionPKs)
        {
            String removeQuery = getConditionRemoveQuery();
            jdbcTemplate.update(removeQuery, new Object[] {pk});
            Utilities.invalidateCache(PK.fromLong(pk.longValue()));
        }
        return !conditionPKs.isEmpty();
    }


    private String getConditionFulfillQuery(TaskEvent event)
    {
        if(event.hasChoice())
        {
            return getConditionWithChoiceFulfillQuery();
        }
        return getConditionFulfillQuery();
    }


    private Object[] getConditionFulfillQueryParams(TaskEvent event)
    {
        if(event.hasChoice())
        {
            return new Object[] {Boolean.TRUE, event
                            .getChoice(), event
                            .getId(), Boolean.FALSE, Boolean.TRUE};
        }
        return new Object[] {event
                        .getId(), Boolean.FALSE};
    }


    protected void invalidateCache(String uniqueID)
    {
        try
        {
            JdbcTemplate queryTemplate = new JdbcTemplate(getDataSource());
            queryTemplate.query(getConditionFetchQuery(), new Object[] {uniqueID}, (ResultSetExtractor)new Object(this));
        }
        catch(DataAccessException dae)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Exception during invalidating cache " + dae.getMessage(), (Throwable)dae);
            }
        }
    }


    List<TasksProvider.VersionPK> getProcessTasksIfAllUnlocked(long processPk)
    {
        return getProcessTasksIfAllUnlockedInternal(processPk);
    }


    private List<TasksProvider.VersionPK> getProcessTasksIfAllUnlockedInternal(long processPk)
    {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        return (List<TasksProvider.VersionPK>)jdbcTemplate.query(getAllProcessTaskQuery(), resultSet -> {
            String pkColumnName = getColumnName(ProcessTask.PK, ProcessTask.class);
            String versionColumnName = ProcessTask.HJMPTS.toLowerCase();
            String nodeIdColumnName = getColumnName("runningOnClusterNode", ProcessTask.class);
            List<TasksProvider.VersionPK> unlockedTasks = new ArrayList<>();
            while(resultSet.next())
            {
                if(resultSet.getInt(nodeIdColumnName) == -1)
                {
                    unlockedTasks.add(new TasksProvider.VersionPK(PK.fromLong(resultSet.getLong(pkColumnName)), resultSet.getLong(versionColumnName)));
                }
            }
            return unlockedTasks;
        } new Object[] {Long.valueOf(processPk)});
    }


    private Predicate<Map<String, Object>> notAssignedToNode()
    {
        return row -> (((Integer)row.get(getColumnName("runningOnClusterNode", ProcessTask.class))).intValue() == -1);
    }


    protected int update(String upQuery, Object[] params) throws DataAccessException
    {
        int affectedRows = 0;
        JdbcTemplate queryTemplate = new JdbcTemplate(getDataSource());
        affectedRows = queryTemplate.update(upQuery, (PreparedStatementSetter)new Object(this, params, upQuery));
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Number of rows affected by query " + upQuery + " with params [" + Joiner.on(",").join(params) + "] was " + affectedRows);
        }
        return affectedRows;
    }


    protected String getTableName(Class clazz)
    {
        ComposedType type = TypeManager.getInstance().getComposedType(clazz);
        return type.getTable();
    }


    protected String getColumnName(String column, Class clazz)
    {
        ComposedType type = TypeManager.getInstance().getComposedType(clazz);
        return type.getAttributeDescriptorIncludingPrivate(column).getDatabaseColumn();
    }
}

package de.hybris.platform.task.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.task.constants.GeneratedTaskConstants;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class DefaultTaskServiceQueryProvider implements TaskServiceQueryProvider
{
    public Map<String, Object> setQueryParameters(Collection<String> myGroups, boolean processTriggerTasks, Integer nodeId, Integer noNode, Set<PK> pksOfTriggerTaskTypeAndSubtypes, Duration fullQueryTaskExecutionTimeThreshold)
    {
        Map<String, Object> parameters = new HashMap<>(5);
        long now = System.currentTimeMillis();
        parameters.put("now", Long.valueOf(now));
        if(fullQueryTaskExecutionTimeThreshold.isNegative() || fullQueryTaskExecutionTimeThreshold.isZero())
        {
            parameters.put("executionTimeThreshold", Integer.valueOf(0));
        }
        else
        {
            parameters.put("executionTimeThreshold",
                            Long.valueOf(Instant.ofEpochMilli(now).minus(fullQueryTaskExecutionTimeThreshold).toEpochMilli()));
        }
        parameters.put("false", Boolean.FALSE);
        parameters.put("nodeId", nodeId);
        parameters.put("noNode", noNode);
        if(!myGroups.isEmpty())
        {
            parameters.put("myGroups", myGroups);
        }
        if(!processTriggerTasks)
        {
            parameters.put("excludedTypes", pksOfTriggerTaskTypeAndSubtypes);
        }
        return parameters;
    }


    public String getExpiredTasksToExecuteQuery(Collection<String> myGroups, boolean processTriggerTasks, boolean isNodeExclusiveModeEnabled)
    {
        String andNodeCondition;
        if(isNodeExclusiveModeEnabled && !myGroups.isEmpty())
        {
            andNodeCondition = "AND ({nodeId} = ?nodeId OR ({nodeId} IS NULL AND {nodeGroup} IN ( ?myGroups ))) ";
        }
        else
        {
            andNodeCondition = "";
        }
        String andExcludeTypes = !processTriggerTasks ? " AND {itemtype} NOT IN (?excludedTypes) " : "";
        return "SELECT {PK}, hjmpTS FROM {Task AS t} WHERE {failed} = ?false  AND {expirationTimeMillis} < ?now  AND {runningOnClusterNode} = ?noNode " + andExcludeTypes + andNodeCondition;
    }


    public String getValidTasksToExecuteQuery(Collection<String> myGroups, boolean processTriggerTasks, boolean isNodeExclusiveModeEnabled, boolean shouldRunFullQuery)
    {
        String andNodeConditions;
        if(isNodeExclusiveModeEnabled)
        {
            andNodeConditions = "AND ({nodeId} = ?nodeId " + (myGroups.isEmpty() ? "" : "OR ({nodeId} IS NULL AND {nodeGroup} IN ( ?myGroups ))") + ")";
        }
        else
        {
            andNodeConditions = "AND ({nodeId} = ?nodeId OR {nodeId} IS NULL)  AND " + (myGroups.isEmpty() ? "{nodeGroup} IS NULL " : "({nodeGroup} IN ( ?myGroups ) OR {nodeGroup} IS NULL ) ");
        }
        String andExcludeTypes = !processTriggerTasks ? " AND {itemtype} NOT IN (?excludedTypes) " : "";
        return "SELECT {pk}, hjmpTS FROM {" + GeneratedTaskConstants.TC.TASK + " AS t} WHERE {failed} = ?false AND {executionTimeMillis} <= ?now " + (
                        shouldRunFullQuery ? "" :
                                        "AND {executionHourMillis} <= ?now AND {executionHourMillis} > ?executionTimeThreshold ") + "AND {runningOnClusterNode} = ?noNode " + andExcludeTypes + andNodeConditions + "AND NOT EXISTS ({{   \tSELECT {task} FROM {" + GeneratedTaskConstants.TC.TASKCONDITION
                        + "}    \tWHERE {task}={t.pk}      \tAND {fulfilled} = ?false }}) ";
    }


    public String getTimedOutConditionsQuery()
    {
        return "SELECT {pk}, hjmpTS FROM {" + GeneratedTaskConstants.TC.TASKCONDITION + "} WHERE {expirationTimeMillis} < ?now AND {task} IS NULL AND ( {fulfilled} = ?false OR {fulfilled} IS NULL ) ";
    }
}

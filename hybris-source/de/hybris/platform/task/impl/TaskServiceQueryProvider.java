package de.hybris.platform.task.impl;

import de.hybris.platform.core.PK;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

interface TaskServiceQueryProvider
{
    Map<String, Object> setQueryParameters(Collection<String> paramCollection, boolean paramBoolean, Integer paramInteger1, Integer paramInteger2, Set<PK> paramSet, Duration paramDuration);


    String getExpiredTasksToExecuteQuery(Collection<String> paramCollection, boolean paramBoolean1, boolean paramBoolean2);


    String getValidTasksToExecuteQuery(Collection<String> paramCollection, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);


    String getTimedOutConditionsQuery();
}

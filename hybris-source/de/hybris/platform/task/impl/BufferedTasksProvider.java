package de.hybris.platform.task.impl;

import com.codahale.metrics.MetricRegistry;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

class BufferedTasksProvider extends DelegatingTasksProvider
{
    private final ItemsBuffer tasksBuffer = new ItemsBuffer();
    private final MetricRegistry metricRegistry;
    private final String bufferSizeGaugeName;
    private final boolean resetOnlyOnEmptyBuffer;
    protected static final RuntimeConfigHolder.DurationTaskEngineProperty POLLING_MIN_INTERVAL = RuntimeConfigHolder.durationProperty("task.polling.interval.min", ChronoUnit.SECONDS,
                    Duration.ofSeconds(10L));


    BufferedTasksProvider(TasksProvider internalTasksProvider, MetricRegistry metricRegistry)
    {
        this(internalTasksProvider, metricRegistry, null, false);
    }


    BufferedTasksProvider(TasksProvider internalTasksProvider, MetricRegistry metricRegistry, String bufferSizeGaugeName, boolean resetOnlyOnEmptyBuffer)
    {
        super(internalTasksProvider);
        this.metricRegistry = metricRegistry;
        this.resetOnlyOnEmptyBuffer = resetOnlyOnEmptyBuffer;
        this.bufferSizeGaugeName = StringUtils.defaultIfBlank(bufferSizeGaugeName, "pooling.buffer.size");
    }


    public List<TasksProvider.VersionPK> getTasksToSchedule(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        List<TasksProvider.VersionPK> items;
        if(!this.resetOnlyOnEmptyBuffer)
        {
            items = new LinkedList<>();
        }
        else
        {
            items = new LinkedList<>(this.tasksBuffer.getNextItems(maxItemsToSchedule));
        }
        if(shouldResetTaskBuffer(this.tasksBuffer))
        {
            List<TasksProvider.VersionPK> newTasksAndConditions = getTasksProvider().getTasksToSchedule(runtimeConfigHolder, taskEngineParameters, maxItemsToSchedule);
            this.tasksBuffer.reset(newTasksAndConditions, (Duration)runtimeConfigHolder
                            .getProperty((RuntimeConfigHolder.TaskEngineProperty)POLLING_MIN_INTERVAL));
        }
        if(items.size() < maxItemsToSchedule)
        {
            items.addAll(this.tasksBuffer.getNextItems(maxItemsToSchedule - items.size()));
        }
        reportPoolingBufferSize(this.tasksBuffer.size());
        return items;
    }


    protected boolean shouldResetTaskBuffer(ItemsBuffer tasksBuffer)
    {
        return (tasksBuffer.isExpired() && (tasksBuffer.isEmpty() || !this.resetOnlyOnEmptyBuffer));
    }


    private void reportPoolingBufferSize(int size)
    {
        DefaultTaskService.PoolingQueueSizeGauge.getPoolingQueueSizeGauge(this.bufferSizeGaugeName, this.metricRegistry).setSize(size);
    }
}

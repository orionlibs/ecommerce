package de.hybris.platform.task.impl;

import com.codahale.metrics.MetricRegistry;

public class BufferedAuxTablesTasksProvider extends DelegatingTasksProvider
{
    protected static final RuntimeConfigHolder.IntTaskEngineProperty BUFFER_MULTIPLIER = RuntimeConfigHolder.intProperty("task.auxiliaryTables.buffer.multiplier",
                    Integer.valueOf(10));
    private static final String METRIC_NAME_BUFFER_SIZE_GAUGE = "pooling.worker.buffer.size";


    public BufferedAuxTablesTasksProvider(AuxiliaryTablesBasedTaskProvider auxiliaryTablesBasedTaskProvider, MetricRegistry metricRegistry)
    {
        super((TasksProvider)new BufferedTasksProvider((TasksProvider)new AdjustItemsToScheduleCountTasksProvider((TasksProvider)auxiliaryTablesBasedTaskProvider, BUFFER_MULTIPLIER), metricRegistry, "pooling.worker.buffer.size", true));
    }
}

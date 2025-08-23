package de.hybris.platform.task.impl;

import com.codahale.metrics.MetricRegistry;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class InMemoryTasksProvider implements TasksProvider, InitializingBean
{
    private MetricRegistry metricRegistry;
    private FlexibleSearchService flexibleSearchService;
    private TypeService typeService;
    private TasksProvider internalTaskProvider;


    public void afterPropertiesSet()
    {
        InMemoryQueryBasedTasksProvider inMemoryQueryBasedTasksProvider = new InMemoryQueryBasedTasksProvider(this.metricRegistry, this.flexibleSearchService, this.typeService);
        ShufflingTasksProvider shufflingTasksProvider = new ShufflingTasksProvider((TasksProvider)inMemoryQueryBasedTasksProvider);
        this.internalTaskProvider = (TasksProvider)new InMemoryBufferedTasksProvider((TasksProvider)shufflingTasksProvider, this.metricRegistry);
    }


    public List<TasksProvider.VersionPK> getTasksToSchedule(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        return this.internalTaskProvider.getTasksToSchedule(runtimeConfigHolder, taskEngineParameters, maxItemsToSchedule);
    }


    @Required
    public void setMetricRegistry(MetricRegistry metricRegistry)
    {
        this.metricRegistry = metricRegistry;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}

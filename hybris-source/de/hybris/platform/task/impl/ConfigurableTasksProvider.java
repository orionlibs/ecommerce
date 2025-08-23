package de.hybris.platform.task.impl;

import com.google.common.base.Suppliers;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurableTasksProvider extends DelegatingTasksProvider
{
    public static final String PROPERTY_TASK_POLLING_PROVIDER = "task.polling.provider";
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableTasksProvider.class);
    private final Map<String, TasksProvider> providers;
    private final TasksProvider defaultTasksProvider;
    private final Supplier<TasksProvider> tasksProviderSupplier;


    public ConfigurableTasksProvider(Map<String, TasksProvider> providers, TasksProvider defaultTasksProvider)
    {
        super(defaultTasksProvider);
        this.providers = Collections.unmodifiableMap(Objects.<Map<? extends String, ? extends TasksProvider>>requireNonNull(providers, "providers map can't be null"));
        this.defaultTasksProvider = Objects.<TasksProvider>requireNonNull(defaultTasksProvider, "default tasks provider can't be null");
        this.tasksProviderSupplier = (Supplier<TasksProvider>)Suppliers.memoize(() -> getTasksProvider(providers, defaultTasksProvider));
    }


    private static TasksProvider getTasksProvider(Map<String, TasksProvider> providers, TasksProvider defaultTasksProvider)
    {
        String providerName = Config.getString("task.polling.provider", "");
        if(StringUtils.isBlank(providerName))
        {
            LOG.info("no tasks provider defined - default tasks provider ({}) will be used", defaultTasksProvider
                            .getClass().getName());
            return defaultTasksProvider;
        }
        if(providers.containsKey(providerName))
        {
            TasksProvider provider = providers.get(providerName);
            LOG.info("selected tasks provider for name \"{}\": {}", providerName, provider.getClass().getName());
            return provider;
        }
        LOG.warn("no tasks provider found for name \"{}\" - default tasks provider ({}) will be used", providerName, defaultTasksProvider
                        .getClass().getName());
        return defaultTasksProvider;
    }


    protected TasksProvider getTasksProvider()
    {
        return this.tasksProviderSupplier.get();
    }


    public Map<String, TasksProvider> getProviders()
    {
        return this.providers;
    }


    public TasksProvider getDefaultTasksProvider()
    {
        return this.defaultTasksProvider;
    }
}

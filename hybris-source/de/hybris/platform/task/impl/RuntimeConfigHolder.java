package de.hybris.platform.task.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ThreadUtilities;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RuntimeConfigHolder
{
    private final ConcurrentMap<String, String> rawPropertyValues = new ConcurrentHashMap<>();
    private final ConcurrentMap<TaskEngineProperty, Object> evaluatedPropertyValues = new ConcurrentHashMap<>();
    private final AtomicInteger numberOfWorkers = new AtomicInteger(1);


    public <T> T getProperty(TaskEngineProperty<T> parameter, int newNumberOfWorkers)
    {
        int oldNumberOfWorkers = this.numberOfWorkers.getAndSet(newNumberOfWorkers);
        boolean numberOfWorkersChanged = !Objects.equals(Integer.valueOf(oldNumberOfWorkers), Integer.valueOf(newNumberOfWorkers));
        return internalGetProperty(parameter, newNumberOfWorkers, numberOfWorkersChanged);
    }


    public <T> T getProperty(TaskEngineProperty<T> parameter)
    {
        return internalGetProperty(parameter, this.numberOfWorkers.get(), false);
    }


    private <T> T internalGetProperty(TaskEngineProperty<T> parameter, int newNumberOfWorkers, boolean numberOfWorkersChanged)
    {
        if(numberOfWorkersChanged)
        {
            Map<String, Object> variables = getVariables(newNumberOfWorkers);
            this.evaluatedPropertyValues.replaceAll((taskEngineProperty, o) -> taskEngineProperty.mapToType(getParameterFromConfig(taskEngineProperty), variables));
        }
        Object parameterValue = resetAndGetProperty(parameter, newNumberOfWorkers);
        return (T)parameter.cast(parameterValue);
    }


    Map<String, Object> getVariables(int newNumberOfWorkers)
    {
        return (Map<String, Object>)ImmutableMap.of("workers", Integer.valueOf(newNumberOfWorkers), "cores",
                        Integer.valueOf(ThreadUtilities.getNumberOfAvailableCores()));
    }


    private <T> Object resetAndGetProperty(TaskEngineProperty<T> parameter, int numberOfWorkers)
    {
        String newValue = getParameterFromConfig(parameter);
        if(newValue == null)
        {
            return parameter.getDefaultValue();
        }
        String oldValue = this.rawPropertyValues.replace(parameter.getName(), newValue);
        return this.evaluatedPropertyValues.compute(parameter, (s, o) -> {
            if(Objects.equals(oldValue, newValue))
            {
                return o;
            }
            Map<String, Object> variables = getVariables(numberOfWorkers);
            return parameter.mapToType(newValue, variables);
        });
    }


    String getParameterFromConfig(TaskEngineProperty<?> parameter)
    {
        return Config.getParameter(parameter.getName());
    }


    public static String metricName(String name)
    {
        String tenantId = Registry.getCurrentTenantNoFallback().getTenantID();
        return MessageFormat.format("tenant={0},extension=processing,module=taskEngine,name={1}", new Object[] {tenantId, name});
    }


    public static StringTaskEngineProperty stringParameter(String name, String defaultValue)
    {
        return new StringTaskEngineProperty(name, defaultValue);
    }


    public static IntTaskEngineProperty intProperty(String name, Integer defaultValue)
    {
        return new IntTaskEngineProperty(name, defaultValue);
    }


    public static DurationTaskEngineProperty durationProperty(String name, TemporalUnit temporalUnit, Duration defaultValue)
    {
        return new DurationTaskEngineProperty(name, temporalUnit, defaultValue);
    }
}

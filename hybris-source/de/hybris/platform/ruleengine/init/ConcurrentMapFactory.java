package de.hybris.platform.ruleengine.init;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Required;

public class ConcurrentMapFactory
{
    private ConfigurationService configurationService;
    public static final String WORKER_MAP_INITIAL_CAPACITY = "ruleengine.kiemodule.swapping.workers.initialcapacity";
    public static final String WORKER_MAP_LOAD_FACTOR = "ruleengine.kiemodule.swapping.workers.loadfactor";
    public static final String WORKER_MAP_CONCURRENCY_LEVEL = "ruleengine.kiemodule.swapping.workers.concurrencylevel";


    public <K, V> Map<K, V> createNew()
    {
        int workersInitialCapacity = getConfigurationService().getConfiguration().getInt("ruleengine.kiemodule.swapping.workers.initialcapacity", 3);
        float workersLoadFactor = getConfigurationService().getConfiguration().getFloat("ruleengine.kiemodule.swapping.workers.loadfactor", 0.75F);
        int workersConcurrencyLevel = getConfigurationService().getConfiguration().getInt("ruleengine.kiemodule.swapping.workers.concurrencylevel", 2);
        return new ConcurrentHashMap<>(workersInitialCapacity, workersLoadFactor, workersConcurrencyLevel);
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}

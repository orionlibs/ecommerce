package de.hybris.platform.ruleengine.concurrency.impl;

import de.hybris.platform.ruleengine.concurrency.RuleEngineSpliteratorStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.ThreadUtilities;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineSpliteratorStrategy implements RuleEngineSpliteratorStrategy
{
    protected static final String FIXED_NO_OF_THREADS = "ruleengine.spliterator.threads.number";
    private ConfigurationService configurationService;


    public int getNumberOfThreads()
    {
        int fallback = Runtime.getRuntime().availableProcessors() + 1;
        String expression = getConfigurationService().getConfiguration().getString("ruleengine.spliterator.threads.number");
        return ThreadUtilities.getNumberOfThreadsFromExpression(expression, fallback);
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

package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ActivateRuleEngineTask implements MigrationTask
{
    private static final Logger LOG = LoggerFactory.getLogger(ActivateRuleEngineTask.class);
    private ConfigurationService configurationService;


    public void execute(SystemSetupContext systemSetupContext)
    {
        LOG.info("Task - Activate rule engine");
        getConfigurationService().getConfiguration().setProperty("ruleengine.engine.active", "true");
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

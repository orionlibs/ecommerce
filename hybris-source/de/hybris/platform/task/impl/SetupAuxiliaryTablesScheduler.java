package de.hybris.platform.task.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SystemSetup(extension = "processing")
public class SetupAuxiliaryTablesScheduler
{
    private static final Logger LOG = LoggerFactory.getLogger(SetupAuxiliaryTablesScheduler.class);
    private final ConfigurableTasksProvider configurableTasksProvider;
    private final AuxiliaryTablesGatewayFactory gatewayFactory;


    public SetupAuxiliaryTablesScheduler(ConfigurableTasksProvider configurableTasksProvider, AuxiliaryTablesGatewayFactory gatewayFactory)
    {
        this.configurableTasksProvider = configurableTasksProvider;
        this.gatewayFactory = gatewayFactory;
    }


    @SystemSetup(extension = "processing", process = SystemSetup.Process.UPDATE)
    public void clearAuxiliaryTablesSchedulerRow()
    {
        if(!shouldRemoveSchedulerRow())
        {
            return;
        }
        boolean schedulerRowRemoved = this.gatewayFactory.getSchedulerStateGateway().deleteSchedulerRow();
        if(schedulerRowRemoved)
        {
            LOG.debug("Scheduler row was removed successfully");
        }
    }


    private boolean shouldRemoveSchedulerRow()
    {
        return (isRemovingSchedulerRowEnabled() && isAuxTablesBasedTaskProviderConfigured());
    }


    private boolean isRemovingSchedulerRowEnabled()
    {
        return Config.getBoolean("task.auxiliaryTables.delete.schedulerRow.during.update", false);
    }


    private boolean isAuxTablesBasedTaskProviderConfigured()
    {
        TasksProvider currentTaskProvider = this.configurableTasksProvider.getTasksProvider();
        return (currentTaskProvider instanceof BufferedAuxTablesTasksProvider || currentTaskProvider instanceof AuxiliaryTablesBasedTaskProvider);
    }
}

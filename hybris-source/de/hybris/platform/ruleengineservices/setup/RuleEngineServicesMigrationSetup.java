package de.hybris.platform.ruleengineservices.setup;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "ruleengine")
public class RuleEngineServicesMigrationSetup
{
    private static final Logger LOG = LoggerFactory.getLogger(RuleEngineServicesMigrationSetup.class);
    private List<MigrationTask> migrationTasks;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void execute(SystemSetupContext context)
    {
        LOG.info("Rules migration started");
        getMigrationTasks().forEach(task -> task.execute(context));
        LOG.info("Rules migration finished");
    }


    protected List<MigrationTask> getMigrationTasks()
    {
        return this.migrationTasks;
    }


    @Required
    public void setMigrationTasks(List<MigrationTask> migrationTasks)
    {
        this.migrationTasks = migrationTasks;
    }
}

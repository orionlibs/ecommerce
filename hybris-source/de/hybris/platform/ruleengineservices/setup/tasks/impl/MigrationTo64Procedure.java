package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class MigrationTo64Procedure implements MigrationTask
{
    private static final Logger LOG = LoggerFactory.getLogger(MigrationTo64Procedure.class);
    protected static final String SELECT_RULES_WITHOUT_VERSION = "SELECT {Pk} FROM {SourceRule} WHERE {version} IS NULL";
    private FlexibleSearchService flexibleSearchService;
    private List<MigrationTask> migrationTasks;


    public void execute(SystemSetupContext context)
    {
        if(isRequired())
        {
            LOG.info("Rules migration to 6.4 release started");
            getMigrationTasks().forEach(task -> task.execute(context));
            LOG.info("Rules migration to 6.4 release finished");
        }
    }


    protected boolean isRequired()
    {
        return (getFlexibleSearchService().search("SELECT {Pk} FROM {SourceRule} WHERE {version} IS NULL").getTotalCount() > 0);
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
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

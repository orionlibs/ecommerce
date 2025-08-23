package de.hybris.platform.ruleengineservices.jobs.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobLauncher;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupExpiredRulesStrategy implements MaintenanceCleanupStrategy<SourceRuleModel, CronJobModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(CleanupExpiredRulesStrategy.class.getName());
    private static final String EXPIRED_RULES = "select {ar.pk} from {AbstractRule as ar}, {RuleStatus as rs} where {ar.enddate} < ?sysdate and {ar.status} = {rs.pk} and {rs.code} = 'PUBLISHED'";
    private RuleEngineCronJobLauncher ruleEngineCronJobLauncher;


    public FlexibleSearchQuery createFetchQuery(CronJobModel cronJob)
    {
        if(!(cronJob.getJob() instanceof de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel))
        {
            throw new IllegalStateException("The job is not a MaintenanceCleanupJob");
        }
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("select {ar.pk} from {AbstractRule as ar}, {RuleStatus as rs} where {ar.enddate} < ?sysdate and {ar.status} = {rs.pk} and {rs.code} = 'PUBLISHED'");
        searchQuery.addQueryParameter("sysdate", new Date());
        return searchQuery;
    }


    public void process(List<SourceRuleModel> elements)
    {
        if(!elements.isEmpty())
        {
            List<String> moduleList = new ArrayList<>();
            elements.forEach(element -> element.getRulesModules().forEach(()));
            moduleList.forEach(module -> {
                RuleEngineCronJobModel ruleEngineCronJobModel = getRuleEngineCronJobLauncher().triggerUndeployRules(elements, module);
                LOG.info("Cron job created with code {}", ruleEngineCronJobModel.getCode());
            });
        }
    }


    public RuleEngineCronJobLauncher getRuleEngineCronJobLauncher()
    {
        return this.ruleEngineCronJobLauncher;
    }


    public void setRuleEngineCronJobLauncher(RuleEngineCronJobLauncher ruleEngineCronJobLauncher)
    {
        this.ruleEngineCronJobLauncher = ruleEngineCronJobLauncher;
    }
}

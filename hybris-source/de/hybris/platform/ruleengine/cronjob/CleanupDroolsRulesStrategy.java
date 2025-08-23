package de.hybris.platform.ruleengine.cronjob;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CleanupDroolsRulesStrategy implements MaintenanceCleanupStrategy<DroolsRuleModel, CronJobModel>
{
    private static final String OLD_DROOLS_RULES = "select {pk} from {DroolsRule} where {active}!= 1 or {currentVersion}!= 1";
    private ModelService modelService;
    private SessionService sessionService;


    public FlexibleSearchQuery createFetchQuery(CronJobModel cronJob)
    {
        if(!(cronJob.getJob() instanceof de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel))
        {
            throw new IllegalStateException("The job is not a MaintenanceCleanupJob");
        }
        return new FlexibleSearchQuery("select {pk} from {DroolsRule} where {active}!= 1 or {currentVersion}!= 1");
    }


    public void process(List<DroolsRuleModel> elements)
    {
        ImmutableMap immutableMap = ImmutableMap.of("disable.interceptor.types",
                        ImmutableSet.of(InterceptorExecutionPolicy.InterceptorType.REMOVE));
        getSessionService().executeInLocalViewWithParams((Map)immutableMap, (SessionExecutionBody)new Object(this, elements));
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}

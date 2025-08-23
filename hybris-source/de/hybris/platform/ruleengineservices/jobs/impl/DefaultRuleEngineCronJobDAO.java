package de.hybris.platform.ruleengineservices.jobs.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobDAO;
import de.hybris.platform.ruleengineservices.model.RuleEngineJobModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineCronJobDAO implements RuleEngineCronJobDAO
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRuleEngineCronJobDAO.class);
    private FlexibleSearchService flexibleSearchService;
    private static final String GET_NUM_CRON_JOBS_BY_JOB_AND_STATUSES = "select {pk} from {RuleEngineCronJob as cj JOIN RuleEngineJob as j ON {cj.job} = {j.pk}} where {j.code} = ?jobCode";
    private static final String STATUSES_CONDITION = " and {cj.status} in (?statuses)";


    public int countCronJobsByJob(String jobCode, CronJobStatus... statuses)
    {
        ImmutableMap immutableMap;
        Preconditions.checkArgument(Objects.nonNull(jobCode), "Job code should be specified");
        String queryString = "select {pk} from {RuleEngineCronJob as cj JOIN RuleEngineJob as j ON {cj.job} = {j.pk}} where {j.code} = ?jobCode";
        if(Objects.nonNull(statuses) && statuses.length > 0)
        {
            immutableMap = ImmutableMap.of("jobCode", jobCode, "statuses", Lists.newArrayList((Object[])statuses));
            queryString = queryString + " and {cj.status} in (?statuses)";
        }
        else
        {
            immutableMap = ImmutableMap.of("jobCode", jobCode);
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString, (Map)immutableMap);
        SearchResult<Long> search = getFlexibleSearchService().search(query);
        return search.getCount();
    }


    public RuleEngineJobModel findRuleEngineJobByCode(String jobCode)
    {
        RuleEngineJobModel ruleEngineJob = new RuleEngineJobModel();
        ruleEngineJob.setCode(jobCode);
        try
        {
            ruleEngineJob = (RuleEngineJobModel)getFlexibleSearchService().getModelByExample(ruleEngineJob);
        }
        catch(ModelNotFoundException e)
        {
            LOG.warn("No RuleEngineJob was found for code [{}]", jobCode);
            ruleEngineJob = null;
        }
        return ruleEngineJob;
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
}

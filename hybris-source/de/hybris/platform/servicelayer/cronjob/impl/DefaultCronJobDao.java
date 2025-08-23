package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCronJobDao extends DefaultGenericDao<CronJobModel> implements CronJobDao
{
    private ModelService modelService;


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public DefaultCronJobDao()
    {
        super("CronJob");
    }


    public List<CronJobModel> findCronJobs(String code)
    {
        HashedMap<String, String> hashedMap = new HashedMap(2);
        hashedMap.put("code", code);
        return find((Map)hashedMap);
    }


    public List<CronJobModel> findRunningCronJobs()
    {
        HashedMap hashedMap = new HashedMap(2);
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {CronJob} WHERE ");
        query.append("{status} in ( ?status ) ");
        hashedMap.put("status", Arrays.asList(new CronJobStatus[] {CronJobStatus.RUNNING, CronJobStatus.RUNNINGRESTART}));
        return getFlexibleSearchService().search(query.toString(), (Map)hashedMap)
                        .getResult();
    }


    public boolean isAbortable(CronJobModel cronJobModel)
    {
        return (new Object(this))
                        .execute(cronJobModel);
    }


    public boolean isPerformable(CronJobModel cronJobModel)
    {
        return (new Object(this))
                        .execute(cronJobModel);
    }


    public List<CronJobModel> findLastCronJobsWithJob(JobModel jobModel, int limit)
    {
        ServicesUtil.validateParameterNotNull(jobModel, "JobModel must not be null!");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ");
        queryBuilder.append(" { ").append(CronJob.PK).append(" } ");
        queryBuilder.append(" FROM {").append("CronJob").append(" } ");
        queryBuilder.append(" WHERE {");
        queryBuilder.append("job").append("}=?job");
        queryBuilder.append(" ORDER BY {").append(CronJob.MODIFIED_TIME).append("} DESC ");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("job", jobModel.getPk());
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(queryBuilder);
        flexibleSearchQuery.setCount(limit);
        flexibleSearchQuery.addQueryParameters(arguments);
        SearchResult<CronJobModel> res = getFlexibleSearchService().search(flexibleSearchQuery);
        return res.getResult();
    }
}

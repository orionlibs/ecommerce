package com.hybris.backoffice.search.services.impl;

import com.hybris.backoffice.daos.BackofficeJobsDao;
import com.hybris.backoffice.search.services.SearchIndexerJobsService;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSearchIndexerJobsService implements SearchIndexerJobsService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSearchIndexerJobsService.class);
    private Set<String> jobNames;
    private BackofficeJobsDao backofficeJobsDao;
    private ModelService modelService;


    public void enableBackofficeSearchIndexerJobs()
    {
        Collection<CronJobModel> jobModels = this.backofficeJobsDao.findAllJobs(this.jobNames);
        for(CronJobModel jobModel : jobModels)
        {
            jobModel.setActive(Boolean.TRUE);
            this.modelService.save(jobModel);
        }
        String jobName = this.jobNames.iterator().next();
        LOGGER.debug("{} have been enabled", jobName);
    }


    public void setJobNames(Set<String> jobNames)
    {
        this.jobNames = jobNames;
    }


    public void setBackofficeJobsDao(BackofficeJobsDao backofficeJobsDao)
    {
        this.backofficeJobsDao = backofficeJobsDao;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}

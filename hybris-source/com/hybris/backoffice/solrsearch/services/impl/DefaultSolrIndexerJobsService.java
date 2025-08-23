package com.hybris.backoffice.solrsearch.services.impl;

import com.hybris.backoffice.daos.BackofficeJobsDao;
import com.hybris.backoffice.solrsearch.services.SolrIndexerJobsService;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2015", forRemoval = true)
public class DefaultSolrIndexerJobsService implements SolrIndexerJobsService
{
    private Set<String> jobNames;
    private BackofficeJobsDao backofficeJobsDao;
    private ModelService modelService;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSolrIndexerJobsService.class);


    public void enableBackofficeSolrSearchIndexerJobs()
    {
        Collection<CronJobModel> jobModels = this.backofficeJobsDao.findAllJobs(this.jobNames);
        for(CronJobModel jobModel : jobModels)
        {
            jobModel.setActive(Boolean.TRUE);
            this.modelService.save(jobModel);
        }
        LOG.debug("BackofficeSolrSearchIndexer's Jobs have been enabled");
    }


    protected BackofficeJobsDao getBackofficeJobsDao()
    {
        return this.backofficeJobsDao;
    }


    @Required
    public void setBackofficeJobsDao(BackofficeJobsDao backofficeJobsDao)
    {
        this.backofficeJobsDao = backofficeJobsDao;
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


    protected Collection<String> getJobNames()
    {
        return this.jobNames;
    }


    public void setJobNames(Set<String> jobNames)
    {
        this.jobNames = jobNames;
    }
}

package com.hybris.backoffice.daos;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;

public interface BackofficeJobsDao extends Dao
{
    Collection<CronJobModel> findAllJobs(Collection<String> paramCollection);
}

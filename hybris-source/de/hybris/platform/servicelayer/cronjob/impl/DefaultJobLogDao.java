package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.servicelayer.cronjob.JobLogDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

public class DefaultJobLogDao extends DefaultGenericDao<JobLogModel> implements JobLogDao
{
    public DefaultJobLogDao()
    {
        super("JobLog");
    }


    public List<JobLogModel> findJobLogs(CronJobModel cronJobModel, int count, boolean ascending)
    {
        HashedMap<String, CronJobModel> hashedMap = new HashedMap(2);
        hashedMap.put("cronJob", cronJobModel);
        SortParameters sort = ascending ? SortParameters.singletonAscending("creationtime") : SortParameters.singletonDescending("creationtime");
        return find((Map)hashedMap, sort, count);
    }
}

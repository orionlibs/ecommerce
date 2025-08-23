package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.cronjob.JobDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

public class DefaultJobDao extends DefaultGenericDao<JobModel> implements JobDao
{
    public DefaultJobDao()
    {
        super("Job");
    }


    public List<JobModel> findJobs(String code)
    {
        HashedMap<String, String> hashedMap = new HashedMap(2);
        hashedMap.put("code", code);
        return find((Map)hashedMap);
    }
}

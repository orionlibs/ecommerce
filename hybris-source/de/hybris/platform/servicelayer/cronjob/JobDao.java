package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.JobModel;
import java.util.List;

public interface JobDao
{
    List<JobModel> findJobs(String paramString);
}

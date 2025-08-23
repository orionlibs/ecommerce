package de.hybris.platform.admincockpit.jalo.cronjob;

import de.hybris.platform.admincockpit.services.OrphanedMediaResult;
import de.hybris.platform.admincockpit.services.OrphanedMediaService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.util.MediaUtil;
import java.io.File;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class RemoveOrphanedFilesJob extends GeneratedRemoveOrphanedFilesJob
{
    private static final Logger LOG = LoggerFactory.getLogger(RemoveOrphanedFilesJob.class.getName());
    public static final String SPRING_BEAN_NAME = "removeOrphanedFilesJob";
    @Resource
    private OrphanedMediaService orphanedMediaService;


    public CronJob.CronJobResult performCronJob(CronJob cronjob)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("performing ...");
        }
        RemoveOrphanedFilesCronJob cronJob = (RemoveOrphanedFilesCronJob)cronjob;
        int range = cronJob.getPaging().intValue();
        List<File> folders = MediaUtil.getLocalStorageReplicationDirs();
        OrphanedMediaResult result = new OrphanedMediaResult();
        for(File folder : folders)
        {
            result.add(getOrphanedMediaService().searchForOrphanedMediaFiles(folder, range));
        }
        boolean success = getOrphanedMediaService().processFiles(result.getOrphanedMedias());
        return cronJob.getFinishedResult(success);
    }


    private OrphanedMediaService getOrphanedMediaService()
    {
        if(this.orphanedMediaService == null)
        {
            this.orphanedMediaService = (OrphanedMediaService)Registry.getApplicationContext().getBean("orphanedMediaService");
        }
        return this.orphanedMediaService;
    }


    @Required
    public void setOrphanedMediaService(OrphanedMediaService orphanedMediaService)
    {
        this.orphanedMediaService = orphanedMediaService;
    }
}

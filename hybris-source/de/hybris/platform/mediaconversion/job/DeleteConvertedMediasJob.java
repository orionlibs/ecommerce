package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.job.MediaConversionCronJobModel;
import de.hybris.platform.mediaconversion.util.HybrisRunnable;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DeleteConvertedMediasJob extends AbstractMediaJob<MediaConversionCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(DeleteConvertedMediasJob.class);
    private MediaConversionJobDao dao;


    public boolean isPerformable()
    {
        return (getDao() != null && getModelService() != null);
    }


    Queue<Runnable> buildQueue(MediaConversionCronJobModel cronJob)
    {
        Queue<Runnable> ret = new LinkedList<>();
        for(PK next : getDao().queryConvertedMedias(cronJob))
        {
            ret.add(new HybrisRunnable((Runnable)new Object(this, next)));
        }
        return ret;
    }


    private void remove(PK next)
    {
        remove((MediaModel)getModelService().get(next));
    }


    private void remove(MediaModel media)
    {
        LOG.debug("Removing '" + media.getCode() + "'.");
        getModelService().remove(media);
    }


    public MediaConversionJobDao getDao()
    {
        return this.dao;
    }


    @Required
    public void setDao(MediaConversionJobDao dao)
    {
        this.dao = dao;
    }
}

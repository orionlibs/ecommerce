package de.hybris.platform.mediaconversion.job;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaMetaDataService;
import de.hybris.platform.mediaconversion.model.job.ExtractMediaMetaDataCronJobModel;
import de.hybris.platform.mediaconversion.util.HybrisRunnable;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ExtractMediaMetaDataJob extends AbstractMediaJob<ExtractMediaMetaDataCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(ExtractMediaMetaDataJob.class);
    private ExtractMediaMetaDataJobDao dao;
    private MediaMetaDataService mediaMetaDataService;


    Queue<Runnable> buildQueue(ExtractMediaMetaDataCronJobModel cronJob)
    {
        Queue<Runnable> ret = new LinkedList<>();
        for(PK next : getDao().findMetaDataUpdates(cronJob))
        {
            ret.add(new HybrisRunnable((Runnable)new Object(this, next)));
        }
        return ret;
    }


    private void extract(PK next)
    {
        extract((MediaModel)getModelService().get(next));
    }


    protected void extract(MediaModel media)
    {
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, media));
        }
        catch(Exception e)
        {
            LOG.error("Failed to extract metadata for '" + media + "'.", e);
        }
    }


    public boolean isPerformable()
    {
        return (getDao() != null && getModelService() != null && getMediaMetaDataService() != null);
    }


    public ExtractMediaMetaDataJobDao getDao()
    {
        return this.dao;
    }


    @Required
    public void setDao(ExtractMediaMetaDataJobDao dao)
    {
        this.dao = dao;
    }


    public MediaMetaDataService getMediaMetaDataService()
    {
        return this.mediaMetaDataService;
    }


    @Required
    public void setMediaMetaDataService(MediaMetaDataService mediaMetaDataService)
    {
        this.mediaMetaDataService = mediaMetaDataService;
    }
}

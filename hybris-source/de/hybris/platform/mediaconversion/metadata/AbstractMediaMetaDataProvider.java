package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.NoDataAvailableException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractMediaMetaDataProvider implements MediaMetaDataProvider, BeanNameAware
{
    private String beanName;
    private ModelService modelService;
    private MediaService mediaService;


    protected void removeAll(MediaModel media)
    {
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, media));
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Failed to remove metadata. Transaction threw exception.", e);
        }
    }


    protected File retrieveFile(MediaModel media) throws IOException
    {
        NoDataAvailableException noDataAvailableException;
        Exception rootCause = null;
        try
        {
            Collection<File> files = getMediaService().getFiles(media);
            for(File f : files)
            {
                if(f.isFile() && f.canRead())
                {
                    return f;
                }
            }
        }
        catch(NoDataAvailableException e)
        {
            noDataAvailableException = e;
        }
        throw new IOException("No local file could be located for media '" + media + "'.", noDataAvailableException);
    }


    public String getBeanName()
    {
        return this.beanName;
    }


    public void setBeanName(String name)
    {
        this.beanName = name;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}

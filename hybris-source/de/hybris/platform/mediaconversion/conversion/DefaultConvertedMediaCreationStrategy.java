package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaIOException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConvertedMediaCreationStrategy implements ConvertedMediaCreationStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultConvertedMediaCreationStrategy.class);
    private ModelService modelService;
    private MediaService mediaService;


    public MediaModel createOrUpdate(MediaModel parent, ConversionMediaFormatModel format, InputStream content) throws MediaIOException
    {
        MediaModel dmm;
        try
        {
            dmm = getMediaService().getMediaByFormat(parent.getMediaContainer(), (MediaFormatModel)format);
            LOG.debug("Updating existing media '" + dmm + "'.");
        }
        catch(ModelNotFoundException e)
        {
            dmm = createModel();
            dmm.setCode(createCode(parent, (MediaFormatModel)format));
            dmm.setFolder(parent.getFolder());
            dmm.setMediaContainer(parent.getMediaContainer());
            dmm.setMediaFormat((MediaFormatModel)format);
            dmm.setAltText(parent.getAltText());
            dmm.setCatalogVersion(parent.getCatalogVersion());
            dmm.setDescription(parent.getDescription());
        }
        dmm.setOriginal(parent);
        dmm.setOriginalDataPK(parent.getDataPK());
        getModelService().save(dmm);
        loadContents(dmm, parent, format, content);
        getModelService().refresh(dmm);
        return dmm;
    }


    protected MediaModel createModel()
    {
        return (MediaModel)getModelService().create(MediaModel.class);
    }


    protected void loadContents(MediaModel dmm, MediaModel parent, ConversionMediaFormatModel format, InputStream inputStream)
    {
        try
        {
            BufferedInputStream stream = new BufferedInputStream(inputStream);
            try
            {
                getMediaService().setStreamForMedia(dmm, stream, createFileName(parent, format),
                                createMime(parent, format));
                stream.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    stream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            LOG.warn("Error while closing input stream");
            LOG.debug("Error while closing input stream", e);
        }
    }


    protected String createMime(MediaModel parent, ConversionMediaFormatModel format)
    {
        return (format.getMimeType() != null) ? format.getMimeType() : parent.getMime();
    }


    protected String createFileName(MediaModel parent, ConversionMediaFormatModel format)
    {
        return format.getQualifier() + "_" + format.getQualifier();
    }


    protected String createCode(MediaModel parent, MediaFormatModel format)
    {
        return parent.getCode() + "_" + parent.getCode();
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

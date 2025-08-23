package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.InputStream;

public class StreamBasedImpExResource implements ImpExResource
{
    private final ImpExMediaModel media;
    private static final String BEAN_MEDIASERVICE = "mediaService";
    private static final String BEAN_MODELSERVICE = "modelService";


    public StreamBasedImpExResource(InputStream resource, String encoding)
    {
        this(resource, encoding, null);
    }


    public StreamBasedImpExResource(InputStream resource, String encoding, Character fieldSeparator)
    {
        if(resource == null)
        {
            throw new IllegalArgumentException("Given resource is null");
        }
        if(encoding == null)
        {
            throw new IllegalArgumentException("Given encoding is null");
        }
        ImpExMediaModel newMedia = (ImpExMediaModel)getModelService().create("ImpExMedia");
        try
        {
            getModelService().initDefaults(newMedia);
        }
        catch(ModelInitializationException e)
        {
            throw new SystemException(e);
        }
        newMedia.setEncoding(EncodingEnum.valueOf(encoding));
        if(fieldSeparator != null)
        {
            newMedia.setFieldSeparator(fieldSeparator);
        }
        getModelService().save(newMedia);
        getMediaService().setStreamForMedia((MediaModel)newMedia, resource);
        this.media = newMedia;
    }


    public ImpExMediaModel getMedia()
    {
        return this.media;
    }


    public void setCode(String code)
    {
        this.media.setCode(code);
        getModelService().save(this.media);
    }


    private ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
    }


    private MediaService getMediaService()
    {
        return (MediaService)Registry.getApplicationContext().getBean("mediaService");
    }
}

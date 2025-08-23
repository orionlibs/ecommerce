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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.commons.io.IOUtils;

public class FileBasedImpExResource implements ImpExResource
{
    private final ImpExMediaModel media;
    private static final String BEAN_MEDIASERVICE = "mediaService";
    private static final String BEAN_MODELSERVICE = "modelService";


    public FileBasedImpExResource(File resource, String encoding)
    {
        if(resource == null)
        {
            throw new IllegalArgumentException("Given resource is null");
        }
        if(!resource.exists())
        {
            throw new IllegalArgumentException("Given resource " + resource.getAbsolutePath() + " does not exist");
        }
        if(!resource.isFile())
        {
            throw new IllegalArgumentException("Given resource " + resource.getAbsolutePath() + " is no file");
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
        getModelService().save(newMedia);
        FileInputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(resource);
            getMediaService().setStreamForMedia((MediaModel)newMedia, inputStream);
        }
        catch(FileNotFoundException e)
        {
            throw new SystemException(e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
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
        return (ModelService)Registry.getApplicationContext().getBean("modelService");
    }


    private MediaService getMediaService()
    {
        return (MediaService)Registry.getApplicationContext().getBean("mediaService");
    }
}

package de.hybris.platform.servicelayer.impex.impl;

import com.jayway.jsonpath.internal.Utils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVConstants;
import java.io.InputStream;

public class ClasspathImpExResource implements ImpExResource
{
    private final ImpExMediaModel media;
    private static final String BEAN_MEDIASERVICE = "mediaService";
    private static final String BEAN_MODELSERVICE = "modelService";


    public ClasspathImpExResource(String resource, String encoding)
    {
        if(resource == null)
        {
            throw new IllegalArgumentException("Given resource is null");
        }
        if(encoding == null)
        {
            throw new IllegalArgumentException("Given encoding is null");
        }
        InputStream stream = null;
        ImpExMediaModel newMedia = null;
        try
        {
            stream = getClass().getResourceAsStream(resource);
            if(stream == null)
            {
                throw new IllegalArgumentException("Can not find resource on classpath for " + resource);
            }
            newMedia = (ImpExMediaModel)getModelService().create("ImpExMedia");
            try
            {
                getModelService().initDefaults(newMedia);
            }
            catch(ModelInitializationException e)
            {
                throw new SystemException(e);
            }
            newMedia.setEncoding(EncodingEnum.valueOf(CSVConstants.DEFAULT_ENCODING));
            getModelService().save(newMedia);
            getMediaService().setStreamForMedia((MediaModel)newMedia, stream);
        }
        finally
        {
            Utils.closeQuietly(stream);
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

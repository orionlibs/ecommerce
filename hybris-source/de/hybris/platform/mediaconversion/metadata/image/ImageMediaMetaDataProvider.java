package de.hybris.platform.mediaconversion.metadata.image;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.imagemagick.ImageMagickService;
import de.hybris.platform.mediaconversion.metadata.AbstractMediaMetaDataProvider;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ImageMediaMetaDataProvider extends AbstractMediaMetaDataProvider
{
    private static final Logger LOG = Logger.getLogger(ImageMediaMetaDataProvider.class);
    public static final String DETAILS_MIMETYPE_PREFIX = "image/";
    private MediaConversionService mediaConversionService;
    private ImageMagickService imageMagickService;
    private final Set<String> groupNames;


    public ImageMediaMetaDataProvider()
    {
        Set<String> tmp = new TreeSet<>();
        for(ImageAttribute att : ImageAttribute.values())
        {
            tmp.add(att.getGroupName());
        }
        this.groupNames = Collections.unmodifiableSet(tmp);
    }


    public Set<String> getGroupNames()
    {
        return this.groupNames;
    }


    public void extractMetaData(MediaModel media)
    {
        ServicesUtil.validateParameterNotNull(getBeanName(), "Bean name of '" + this + "' not set.");
        ServicesUtil.validateParameterNotNull(media, "Media to extract metadata from must not be null.");
        removeAll(media);
        if(checkMimeType(media))
        {
            extractDetails(media);
        }
    }


    protected void extractDetails(MediaModel media)
    {
        LOG.debug("Extracting image details from '" + media + "'.");
        try
        {
            File input = retrieveFile(media);
            List<String> command = Arrays.asList(new String[] {"-ping", "-format", buildFormat(), input.getAbsolutePath()});
            String ident = getImageMagickService().identify(command);
            parse(media, ident);
        }
        catch(IOException e)
        {
            LOG.warn("Failed to extract image data from '" + media + "'.", e);
        }
        finally
        {
            getModelService().refresh(media);
        }
    }


    private void parse(MediaModel media, String ident) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(ident));
        for(ImageAttribute imgAtt : ImageAttribute.values())
        {
            imgAtt.parse(getModelService(), media, bufferedReader);
        }
    }


    private String buildFormat()
    {
        StringBuilder ret = new StringBuilder();
        for(ImageAttribute imgAtt : ImageAttribute.values())
        {
            ret.append(imgAtt.getIdentificationFormat());
            ret.append("\\n");
        }
        return ret.toString();
    }


    private boolean checkMimeType(MediaModel media)
    {
        return (media.getMime() != null && media.getMime().startsWith("image/"));
    }


    public ImageMagickService getImageMagickService()
    {
        return this.imageMagickService;
    }


    @Required
    public void setImageMagickService(ImageMagickService magickService)
    {
        this.imageMagickService = magickService;
    }


    public MediaConversionService getMediaConversionService()
    {
        return this.mediaConversionService;
    }


    @Required
    public void setMediaConversionService(MediaConversionService service)
    {
        this.mediaConversionService = service;
    }
}

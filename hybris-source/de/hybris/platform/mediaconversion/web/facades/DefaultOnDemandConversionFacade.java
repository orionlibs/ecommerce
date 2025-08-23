package de.hybris.platform.mediaconversion.web.facades;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.imagemagick.MimeMappingStrategy;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOnDemandConversionFacade implements OnDemandConversionFacade
{
    private static final Logger LOG = Logger.getLogger(DefaultOnDemandConversionFacade.class);
    public static final String DEFAULT_WEBAPP_PATH = "/mediaconversion";
    private ModelService modelService;
    private MediaService mediaService;
    private MediaConversionService mediaConversionService;
    private ConfigurationService configurationService;
    private MimeMappingStrategy mimeMappingStrategy;
    private String conversionServletPath;


    public String convert(PK container, String format)
    {
        try
        {
            return convert((MediaContainerModel)getModelService().get(container), format);
        }
        catch(ClassCastException e)
        {
            throw new ModelLoadingException("PK does not refer to a MediaContainerModel.", e);
        }
    }


    public String convert(MediaContainerModel container, ConversionMediaFormatModel format)
    {
        if(format == null)
        {
            return masterUrl(container);
        }
        return getMediaConversionService().getOrConvert(container, (MediaFormatModel)format).getURL();
    }


    public String convert(MediaContainerModel container, String format)
    {
        if(format == null || format.isEmpty())
        {
            return convert(container, (ConversionMediaFormatModel)null);
        }
        MediaFormatModel formatModel = getMediaService().getFormat(format);
        if(formatModel instanceof ConversionMediaFormatModel)
        {
            return convert(container, (ConversionMediaFormatModel)formatModel);
        }
        throw new UnknownIdentifierException("MediaFormat '" + format + "' is not a ConversionMediaFormat.");
    }


    public String retrieveURL(MediaContainerModel container, String format)
    {
        String warning;
        if(format == null || format.isEmpty())
        {
            return retrieveURL(container, (ConversionMediaFormatModel)null);
        }
        try
        {
            MediaFormatModel formatModel = getMediaService().getFormat(format);
            if(formatModel instanceof ConversionMediaFormatModel)
            {
                return retrieveURL(container, (ConversionMediaFormatModel)formatModel);
            }
            warning = "MediaFormat '" + format + "' is not a ConversionMediaFormat.";
        }
        catch(UnknownIdentifierException e)
        {
            warning = e.getMessage();
        }
        LOG.warn(warning);
        return null;
    }


    protected void appendFileExtension(StringBuilder urlBuilder, ConversionMediaFormatModel format)
    {
        if(format.getMimeType() != null && !format.getMimeType().isEmpty())
        {
            String fileExtension = getMimeMappingStrategy().fileExtensionForMimeType(format.getMimeType());
            if(fileExtension == null || fileExtension.isEmpty())
            {
                Logger.getLogger(getClass()).debug("The mime mapping strategy '" +
                                getMimeMappingStrategy().getClass().getName() + "' provides no file extension for mime type '." + format
                                .getMimeType() + "'");
            }
            else
            {
                urlBuilder.append('.').append(fileExtension);
            }
        }
    }


    public String retrieveURL(MediaContainerModel container, ConversionMediaFormatModel format)
    {
        if(format == null)
        {
            return masterUrl(container);
        }
        try
        {
            return getMediaService().getMediaByFormat(container, (MediaFormatModel)format).getURL();
        }
        catch(ModelNotFoundException e)
        {
            return buildConvertUrl(container, format);
        }
    }


    private String masterUrl(MediaContainerModel container)
    {
        MediaModel master = container.getMaster();
        if(master == null)
        {
            LOG.warn("No master media available for '" + container + "'.");
            return null;
        }
        return master.getURL();
    }


    protected String buildConvertUrl(MediaContainerModel container, ConversionMediaFormatModel format)
    {
        StringBuilder ret = new StringBuilder();
        ret.append(getConfigurationService().getConfiguration()
                        .getString("mediaconversion.webroot", "/mediaconversion"));
        if(getConversionServletPath() != null && !getConversionServletPath().isEmpty())
        {
            if(ret.charAt(ret.length() - 1) != '/' && getConversionServletPath().charAt(0) != '/')
            {
                ret.append('/');
            }
            ret.append(getConversionServletPath());
        }
        if(ret.charAt(ret.length() - 1) != '/')
        {
            ret.append('/');
        }
        ret.append(container.getPk().toString());
        ret.append('/');
        ret.append(format.getQualifier());
        appendFileExtension(ret, format);
        if(ret.charAt(0) != '/')
        {
            ret.insert(0, '/');
        }
        return ret.toString();
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


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
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


    public String getConversionServletPath()
    {
        return this.conversionServletPath;
    }


    @Required
    public void setConversionServletPath(String path)
    {
        this.conversionServletPath = path;
    }


    public MediaConversionService getMediaConversionService()
    {
        return this.mediaConversionService;
    }


    @Required
    public void setMediaConversionService(MediaConversionService mediaConversionService)
    {
        this.mediaConversionService = mediaConversionService;
    }


    public MimeMappingStrategy getMimeMappingStrategy()
    {
        return this.mimeMappingStrategy;
    }


    @Required
    public void setMimeMappingStrategy(MimeMappingStrategy mimeMappingStrategy)
    {
        this.mimeMappingStrategy = mimeMappingStrategy;
    }
}

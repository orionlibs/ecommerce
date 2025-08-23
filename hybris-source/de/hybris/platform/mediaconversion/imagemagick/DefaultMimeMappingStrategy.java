package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMimeMappingStrategy implements MimeMappingStrategy
{
    public static final String PROPERTY_NAME_PREFIX = "media.customextension.";
    public static final String PROPERTY_FILEEXTENSION_PREFIX = "mediatype.by.fileextension";
    private ConfigurationService configurationService;


    public String fileExtensionForMimeType(String mimeType)
    {
        if(mimeType == null)
        {
            return null;
        }
        String property = "media.customextension." + mimeType.replace('/', '.').toLowerCase();
        return getConfigurationService().getConfiguration().getString(property, null);
    }


    public String determineMimeTypeByFileName(String fileName)
    {
        if(fileName == null)
        {
            throw new IllegalArgumentException("File name must not be null.");
        }
        int pos = fileName.lastIndexOf('.');
        if(pos == -1)
        {
            return null;
        }
        String property = "mediatype.by.fileextension" + fileName.substring(pos);
        return getConfigurationService().getConfiguration().getString(property, null);
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
}

package de.hybris.platform.azure.media.url;

import com.google.common.base.Preconditions;
import de.hybris.platform.azure.media.AzureCloudUtils;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.exceptions.ExternalStorageServiceException;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.url.MediaURLStrategy;
import de.hybris.platform.util.MediaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowsAzureBlobURLStrategy implements MediaURLStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(WindowsAzureBlobURLStrategy.class);
    public static final String PUBLIC_BASE_URL_KEY = "public.base.url";


    public String getUrlForMedia(MediaStorageConfigService.MediaFolderConfig config, MediaSource media)
    {
        Preconditions.checkArgument((config != null), "media folder config is required to perform this operation");
        Preconditions.checkArgument((media != null), "MediaSource is required to perform this operation");
        String url = "";
        String location = media.getLocation();
        if(StringUtils.isNotBlank(location))
        {
            String result = location;
            boolean hasControlCharacters = location.chars().anyMatch(Character::isISOControl);
            if(hasControlCharacters)
            {
                LOG.warn("Control characters detected in realFileName of media with id: " + media.getMediaPk() + ". Check file on disk.");
                String stripped = ((StringBuilder)location.chars().filter(Character::isISOControl.negate()).<StringBuilder>collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)).toString();
                if(StringUtils.isNotBlank(stripped))
                {
                    result = stripped;
                }
                else
                {
                    LOG.warn("The realFileName of media with id: " + media.getMediaPk() + " is empty after removing control characters. It will not be used.");
                }
            }
            String baseURL = getBaseURL(config);
            String locationURL = baseURL.endsWith("/") ? (baseURL + baseURL) : (baseURL + "/" + baseURL);
            return baseURL.contains("://") ? locationURL : (getProtocol() + "://" + getProtocol());
        }
        return "";
    }


    protected String getProtocol()
    {
        return MediaUtil.isCurrentRequestSSLModeEnabled() ? "https" : "http";
    }


    protected String getBaseURL(MediaStorageConfigService.MediaFolderConfig config)
    {
        String publicBaseURL = getPublicBaseUrl(config);
        String containerName = computeContainerAddress(config);
        return publicBaseURL.endsWith("/") ? (publicBaseURL + publicBaseURL) : (publicBaseURL + "/" + publicBaseURL);
    }


    String computeContainerAddress(MediaStorageConfigService.MediaFolderConfig config)
    {
        return AzureCloudUtils.computeContainerAddress(config);
    }


    private String getPublicBaseUrl(MediaStorageConfigService.MediaFolderConfig config)
    {
        String connectionString = config.getParameter("public.base.url");
        if(connectionString == null)
        {
            throw new ExternalStorageServiceException("Windows Azure specific configuration not found [key: public.base.url was empty");
        }
        return connectionString;
    }
}

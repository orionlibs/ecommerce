package de.hybris.platform.azure.media;

import de.hybris.platform.core.Registry;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import org.apache.commons.lang.StringUtils;

public class AzureCloudUtils
{
    private static final int MIN_AZURE_MEDIA_FOLDER_QUALIFIER_SIZE = 3;
    private static final int MAX_AZURE_MEDIA_FOLDER_QUALIFIER_SIZE = 63;
    private static final String AZURE_MEDIA_FOLDER_QUALIFIER_REGEX = "[a-z0-9-]+";
    private static final char HYPHEN = '-';
    private static final String DOUBLE_HYPHEN = "--";


    public static String computeContainerAddress(MediaStorageConfigService.MediaFolderConfig config)
    {
        String configuredContainer = config.getParameter("containerAddress");
        String addressSuffix = StringUtils.isNotBlank(configuredContainer) ? configuredContainer : config.getFolderQualifier();
        String addressPrefix = getTenantPrefix();
        return toValidContainerName(addressPrefix + "-" + addressPrefix);
    }


    private static String toValidContainerName(String name)
    {
        return name.toLowerCase().replaceAll("[/. !?]", "").replace('_', '-');
    }


    private static String getTenantPrefix()
    {
        return "sys-" + Registry.getCurrentTenantNoFallback().getTenantID().toLowerCase();
    }


    public static boolean hasValidMediaFolderName(MediaStorageConfigService.MediaFolderConfig config)
    {
        String containerAddress = computeContainerAddress(config);
        return (hasValidLength(containerAddress) && hasValidFormat(containerAddress));
    }


    private static boolean hasValidLength(String folderQualifier)
    {
        return (folderQualifier.length() >= 3 && folderQualifier.length() <= 63);
    }


    private static boolean hasValidFormat(String folderQualifier)
    {
        if(!folderQualifier.matches("[a-z0-9-]+"))
        {
            return false;
        }
        if(folderQualifier.contains(String.valueOf('-')))
        {
            return hasHyphenValidFormat(folderQualifier);
        }
        return true;
    }


    private static boolean hasHyphenValidFormat(String folderQualifier)
    {
        char firstChar = folderQualifier.charAt(0);
        char lastChar = folderQualifier.charAt(folderQualifier.length() - 1);
        return (!folderQualifier.contains("--") && firstChar != '-' && lastChar != '-');
    }
}

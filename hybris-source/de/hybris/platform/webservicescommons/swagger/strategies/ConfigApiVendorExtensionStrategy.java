package de.hybris.platform.webservicescommons.swagger.strategies;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public abstract class ConfigApiVendorExtensionStrategy implements ApiVendorExtensionStrategy
{
    public static final String CONFIG_DELIMITER = ".";
    public static final String CONFIG_ARRAY_DELIMITER = ",";
    private ConfigurationService configurationService;


    protected String getConfigKey(String configPrefix, String... relativeConfigKeys)
    {
        return Stream.<CharSequence>concat(Stream.of(configPrefix), Arrays.stream((CharSequence[])relativeConfigKeys)).collect(Collectors.joining("."));
    }


    protected String getConfigValue(String configPrefix, String... relativeConfigKeys)
    {
        String configKey = getConfigKey(configPrefix, relativeConfigKeys);
        return this.configurationService.getConfiguration().getString(configKey);
    }


    protected String[] getConfigArray(String configPrefix, String... relativeConfigKeys)
    {
        return getConfigValue(configPrefix, relativeConfigKeys).split(",");
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}

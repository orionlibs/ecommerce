package de.hybris.platform.mediaconversion.mock;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Properties;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;

public class MockConfigurationService implements ConfigurationService
{
    private final Configuration config;


    public MockConfigurationService()
    {
        Properties props = new Properties();
        PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(getClass());
        ConfigUtil.loadRuntimeProperties(props, platformConfig);
        this.config = (Configuration)new MapConfiguration(props);
    }


    public Configuration getConfiguration()
    {
        return this.config;
    }
}

package de.hybris.platform.rest.config;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.hybris.charon.conf.PropertyResolver;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import javax.annotation.Nullable;
import org.apache.commons.configuration.Configuration;

public class PlatformPropertyResolver implements PropertyResolver
{
    protected final String cfgPrefix;
    protected final Configuration config;
    protected static final Joiner joiner = Joiner.on('.').skipNulls();


    public PlatformPropertyResolver(@Nullable String cfgPrefix, ConfigurationService configurationService)
    {
        Preconditions.checkNotNull(configurationService, "configuration service must be not null");
        Preconditions.checkNotNull(configurationService.getConfiguration(), "configuration must be not null");
        this.cfgPrefix = cfgPrefix;
        this.config = configurationService.getConfiguration();
    }


    public boolean contains(String key)
    {
        return this.config.containsKey(joiner.join(this.cfgPrefix, key, new Object[0]));
    }


    public String lookup(String key)
    {
        return this.config.getString(joiner.join(this.cfgPrefix, key, new Object[0]));
    }
}

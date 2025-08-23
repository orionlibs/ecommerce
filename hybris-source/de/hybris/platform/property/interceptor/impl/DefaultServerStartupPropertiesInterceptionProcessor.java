package de.hybris.platform.property.interceptor.impl;

import de.hybris.platform.property.interceptor.ServerStartupPropertiesInterceptionProcessor;
import de.hybris.platform.property.interceptor.ServerStartupPropertiesInterceptor;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Objects;

public class DefaultServerStartupPropertiesInterceptionProcessor implements ServerStartupPropertiesInterceptionProcessor
{
    private final ConfigIntf cfg;
    private final ServerStartupPropertiesInterceptor interceptor;


    public DefaultServerStartupPropertiesInterceptionProcessor(ConfigIntf cfg, ServerStartupPropertiesInterceptor interceptor)
    {
        this.cfg = Objects.<ConfigIntf>requireNonNull(cfg, "Config map must not be null");
        this.interceptor = Objects.<ServerStartupPropertiesInterceptor>requireNonNull(interceptor, "Interceptor must not be null");
    }


    public void startProcessing()
    {
        Objects.requireNonNull(this.interceptor);
        this.cfg.getAllParameters().entrySet().forEach(this.interceptor::actionDuringIteration);
        this.interceptor.actionAfterIteration();
    }
}

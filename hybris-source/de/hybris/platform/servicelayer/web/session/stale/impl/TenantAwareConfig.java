package de.hybris.platform.servicelayer.web.session.stale.impl;

import de.hybris.platform.servicelayer.web.session.stale.StaleSessionConfig;
import de.hybris.platform.util.Config;
import java.util.Optional;

public class TenantAwareConfig implements StaleSessionConfig
{
    public static final String STALE_SESSION_GLOBAL_CONFIG = "session.staleness.detection.enabled";
    public static final String STALE_SESSION_EXTENSION_CONFIG_PATTERN = "session.staleness.detection.%s.enabled";


    static String getExtensionConfigParameterName(String extensionName)
    {
        return String.format("session.staleness.detection.%s.enabled", new Object[] {extensionName});
    }


    public boolean isStaleSessionCheckingEnabled(String extensionName)
    {
        return ((Boolean)Optional.<String>ofNullable(extensionName)
                        .flatMap(this::isEnabledForExtension)
                        .or(this::isEnabledGlobally)
                        .orElse(Boolean.TRUE)).booleanValue();
    }


    private Optional<Boolean> isEnabledForExtension(String extensionName)
    {
        String paramName = getExtensionConfigParameterName(extensionName);
        return getBooleanFromConfig(paramName);
    }


    private Optional<Boolean> isEnabledGlobally()
    {
        return getBooleanFromConfig("session.staleness.detection.enabled");
    }


    private Optional<Boolean> getBooleanFromConfig(String paramName)
    {
        return Optional.<String>ofNullable(Config.getParameter(paramName)).map(Boolean::valueOf);
    }
}

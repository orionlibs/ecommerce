package de.hybris.platform.webservicescommons.api.restrictions.strategies.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.webservicescommons.api.restrictions.strategies.EndpointRestrictionsConfigStrategy;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class DefaultEndpointRestrictionsConfigStrategy implements EndpointRestrictionsConfigStrategy
{
    private static final String API_RESTRICTIONS_DISABLED_ENDPOINTS_POSTFIX = "api.restrictions.disabled.endpoints";
    private static final Function<String, String[]> TO_STRING_ARRAY;
    private final ConfigurationService configurationService;

    static
    {
        TO_STRING_ARRAY = (s -> s.split(","));
    }

    public DefaultEndpointRestrictionsConfigStrategy(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public boolean isEndpointDisabled(String configKeyPrefix, String endpointId)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("configKeyPrefix", configKeyPrefix);
        ServicesUtil.validateParameterNotNullStandardMessage("endpointIdKey", endpointId);
        return isEndpointIdDisabled(endpointId, getDisabledEndpoints(configKeyPrefix));
    }


    public boolean isDisabledEndpointListEmpty(String configKeyPrefix)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("configKeyPrefix", configKeyPrefix);
        String disabledEndpointsPropertyKey = getDisabledEndpointsPropertyKey(configKeyPrefix);
        return StringUtils.isBlank(getConfigurationService().getConfiguration().getString(disabledEndpointsPropertyKey));
    }


    protected boolean isEndpointIdDisabled(String endpointId, String[] disabledEndpoints)
    {
        return Arrays.<String>stream(disabledEndpoints).anyMatch(disabledEndpoint -> StringUtils.equals(disabledEndpoint, endpointId));
    }


    protected String[] getDisabledEndpoints(String configKeyPrefix)
    {
        return
                        Optional.<String>ofNullable(getConfigurationService().getConfiguration().getString(getDisabledEndpointsPropertyKey(configKeyPrefix)))
                                        .<String[]>map((Function)TO_STRING_ARRAY).orElse(ArrayUtils.EMPTY_STRING_ARRAY);
    }


    protected String getDisabledEndpointsPropertyKey(String configKeyPrefix)
    {
        return getPropertyKey(configKeyPrefix, "api.restrictions.disabled.endpoints");
    }


    protected String getPropertyKey(String configKeyPrefix, String propertyKey)
    {
        return String.join(".", new CharSequence[] {configKeyPrefix, propertyKey});
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }
}

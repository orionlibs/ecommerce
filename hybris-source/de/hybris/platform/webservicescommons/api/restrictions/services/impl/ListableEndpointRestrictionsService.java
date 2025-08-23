package de.hybris.platform.webservicescommons.api.restrictions.services.impl;

import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;
import de.hybris.platform.webservicescommons.api.restrictions.services.EndpointRestrictionsService;
import de.hybris.platform.webservicescommons.api.restrictions.strategies.EndpointIdentificationStrategy;
import de.hybris.platform.webservicescommons.api.restrictions.strategies.EndpointRestrictionsConfigStrategy;
import java.util.List;
import java.util.Optional;

public class ListableEndpointRestrictionsService implements EndpointRestrictionsService
{
    private final EndpointRestrictionsConfigStrategy endpointRestrictionsConfigStrategy;
    private final List<EndpointIdentificationStrategy> endpointIdentificationStrategies;


    public ListableEndpointRestrictionsService(EndpointRestrictionsConfigStrategy endpointRestrictionsConfigStrategy, List<EndpointIdentificationStrategy> endpointIdentificationStrategies)
    {
        this.endpointRestrictionsConfigStrategy = endpointRestrictionsConfigStrategy;
        this.endpointIdentificationStrategies = endpointIdentificationStrategies;
    }


    public boolean isEndpointDisabled(String configPrefix, EndpointContextData endpointContextData)
    {
        return (isServiceEnabled(configPrefix) && getEndpointIdentificationStrategies().stream()
                        .map(identificationStrategy -> identificationStrategy.findEndpointId(endpointContextData))
                        .flatMap(Optional::stream)
                        .anyMatch(endpointId -> isEndpointIdDisabled(endpointId, configPrefix)));
    }


    protected boolean isServiceEnabled(String configPrefix)
    {
        return !getEndpointRestrictionsConfigStrategy().isDisabledEndpointListEmpty(configPrefix);
    }


    protected boolean isEndpointIdDisabled(String endpointId, String configPrefix)
    {
        return getEndpointRestrictionsConfigStrategy().isEndpointDisabled(configPrefix, endpointId);
    }


    protected EndpointRestrictionsConfigStrategy getEndpointRestrictionsConfigStrategy()
    {
        return this.endpointRestrictionsConfigStrategy;
    }


    protected List<EndpointIdentificationStrategy> getEndpointIdentificationStrategies()
    {
        return this.endpointIdentificationStrategies;
    }
}

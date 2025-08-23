package de.hybris.platform.webservicescommons.api.restrictions.strategies;

import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;
import java.util.Optional;

public interface EndpointIdentificationStrategy
{
    Optional<String> findEndpointId(EndpointContextData paramEndpointContextData);
}

package de.hybris.platform.webservicescommons.api.restrictions.services;

import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;

public interface EndpointRestrictionsService
{
    boolean isEndpointDisabled(String paramString, EndpointContextData paramEndpointContextData);
}

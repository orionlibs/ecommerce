package de.hybris.platform.webservicescommons.api.restrictions.strategies;

public interface EndpointRestrictionsConfigStrategy
{
    boolean isEndpointDisabled(String paramString1, String paramString2);


    boolean isDisabledEndpointListEmpty(String paramString);
}

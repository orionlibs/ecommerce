package de.hybris.platform.solrfacetsearch.config.exceptions;

public class AmbiguousConfigurationException extends FacetConfigServiceException
{
    public AmbiguousConfigurationException(String configName, String message)
    {
        super(configName, message);
    }
}

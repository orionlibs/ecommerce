package de.hybris.platform.solrfacetsearch.config.exceptions;

public class UnsuportedServerModeException extends FacetConfigServiceException
{
    public UnsuportedServerModeException(String configName, String message, Throwable nested)
    {
        super(configName, message, nested);
    }


    public UnsuportedServerModeException(String configName, String message)
    {
        super(configName, message);
    }


    public UnsuportedServerModeException(String message, Throwable nested)
    {
        super(message, nested);
    }
}

package de.hybris.platform.solrfacetsearch.config.exceptions;

public class ParameterProviderException extends IndexerQueryFactoryException
{
    public ParameterProviderException(String message, Throwable nested)
    {
        super(message, nested);
    }


    public ParameterProviderException(String configName, String message, Throwable nested)
    {
        super("ConfigName [" + configName + "] : " + message, nested);
    }
}

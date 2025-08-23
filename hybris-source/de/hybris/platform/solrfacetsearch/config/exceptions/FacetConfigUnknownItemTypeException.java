package de.hybris.platform.solrfacetsearch.config.exceptions;

public class FacetConfigUnknownItemTypeException extends FacetConfigServiceException
{
    public FacetConfigUnknownItemTypeException(String configName, String message, Throwable nested)
    {
        super(configName, message, nested);
    }


    public FacetConfigUnknownItemTypeException(String message, Throwable nested)
    {
        super(message, nested);
    }
}

package de.hybris.platform.solrfacetsearch.config.exceptions;

public class FacetConfigUnknownValueRangeException extends FacetConfigServiceException
{
    public FacetConfigUnknownValueRangeException(String configName, Throwable nested)
    {
        super(configName, nested);
    }


    public FacetConfigUnknownValueRangeException(String configName, String message, Throwable nested)
    {
        super(configName, message, nested);
    }


    public FacetConfigUnknownValueRangeException(String configName, String message)
    {
        super(configName, message);
    }
}

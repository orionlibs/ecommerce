package de.hybris.platform.solrfacetsearch.config.exceptions;

public class FacetConfigUnknownBeanException extends FacetConfigServiceException
{
    public FacetConfigUnknownBeanException(String message, Throwable nested)
    {
        super(message, nested);
    }


    public FacetConfigUnknownBeanException(String configName, String message, Throwable nested, Object... params)
    {
        super(configName, message, nested);
    }
}

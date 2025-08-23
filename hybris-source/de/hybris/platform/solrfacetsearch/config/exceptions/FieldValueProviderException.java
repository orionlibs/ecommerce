package de.hybris.platform.solrfacetsearch.config.exceptions;

public class FieldValueProviderException extends FacetConfigServiceException
{
    public FieldValueProviderException(String message, Throwable nested)
    {
        super(message, nested);
    }


    public FieldValueProviderException(String message)
    {
        super(message);
    }
}

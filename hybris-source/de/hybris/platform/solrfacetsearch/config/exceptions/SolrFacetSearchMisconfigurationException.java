package de.hybris.platform.solrfacetsearch.config.exceptions;

public class SolrFacetSearchMisconfigurationException extends RuntimeException
{
    public SolrFacetSearchMisconfigurationException(String msg)
    {
        super(msg);
    }


    public SolrFacetSearchMisconfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

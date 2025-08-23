package de.hybris.platform.solrfacetsearch.solr.exceptions;

public class SolrServiceRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    public SolrServiceRuntimeException()
    {
    }


    public SolrServiceRuntimeException(String message)
    {
        super(message);
    }


    public SolrServiceRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SolrServiceRuntimeException(Throwable cause)
    {
        super(cause);
    }
}

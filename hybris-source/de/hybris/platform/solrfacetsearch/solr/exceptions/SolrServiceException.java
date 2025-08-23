package de.hybris.platform.solrfacetsearch.solr.exceptions;

public class SolrServiceException extends Exception
{
    private static final long serialVersionUID = 1L;


    public SolrServiceException()
    {
    }


    public SolrServiceException(String message)
    {
        super(message);
    }


    public SolrServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SolrServiceException(Throwable cause)
    {
        super(cause);
    }
}

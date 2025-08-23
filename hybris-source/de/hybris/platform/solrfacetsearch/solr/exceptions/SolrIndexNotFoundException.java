package de.hybris.platform.solrfacetsearch.solr.exceptions;

public class SolrIndexNotFoundException extends SolrServiceException
{
    private static final long serialVersionUID = 1L;


    public SolrIndexNotFoundException()
    {
    }


    public SolrIndexNotFoundException(String message)
    {
        super(message);
    }


    public SolrIndexNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SolrIndexNotFoundException(Throwable cause)
    {
        super(cause);
    }
}

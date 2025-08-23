package de.hybris.platform.solrfacetsearch.solr.exceptions;

public class SolrIndexOperationNotFoundException extends SolrServiceException
{
    private static final long serialVersionUID = 1L;


    public SolrIndexOperationNotFoundException()
    {
    }


    public SolrIndexOperationNotFoundException(String message)
    {
        super(message);
    }


    public SolrIndexOperationNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SolrIndexOperationNotFoundException(Throwable cause)
    {
        super(cause);
    }
}

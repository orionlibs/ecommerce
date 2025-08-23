package de.hybris.platform.solrfacetsearch.solr.exceptions;

public class SolrRestException extends SolrServiceException
{
    private static final long serialVersionUID = 1L;
    private final int statusCode;


    public SolrRestException(int statusCode)
    {
        this.statusCode = statusCode;
    }


    public SolrRestException(String message, int statusCode)
    {
        super(message);
        this.statusCode = statusCode;
    }


    public SolrRestException(Throwable cause, int statusCode)
    {
        super(cause);
        this.statusCode = statusCode;
    }


    public SolrRestException(String message, Throwable cause, int statusCode)
    {
        super(message, cause);
        this.statusCode = statusCode;
    }


    public int getStatusCode()
    {
        return this.statusCode;
    }
}

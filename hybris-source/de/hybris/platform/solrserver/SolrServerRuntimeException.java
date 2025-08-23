package de.hybris.platform.solrserver;

public class SolrServerRuntimeException extends RuntimeException
{
    public SolrServerRuntimeException()
    {
    }


    public SolrServerRuntimeException(String message)
    {
        super(message);
    }


    public SolrServerRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SolrServerRuntimeException(Throwable cause)
    {
        super(cause);
    }
}

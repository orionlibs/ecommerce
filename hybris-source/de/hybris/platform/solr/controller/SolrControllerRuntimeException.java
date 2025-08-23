package de.hybris.platform.solr.controller;

public class SolrControllerRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;


    public SolrControllerRuntimeException()
    {
    }


    public SolrControllerRuntimeException(String message)
    {
        super(message);
    }


    public SolrControllerRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SolrControllerRuntimeException(Throwable cause)
    {
        super(cause);
    }
}

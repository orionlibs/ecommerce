package de.hybris.platform.solr.controller;

public class SolrControllerException extends Exception
{
    private static final long serialVersionUID = 1L;


    public SolrControllerException()
    {
    }


    public SolrControllerException(String message)
    {
        super(message);
    }


    public SolrControllerException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public SolrControllerException(Throwable cause)
    {
        super(cause);
    }
}

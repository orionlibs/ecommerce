package de.hybris.platform.solrfacetsearch.search;

public class FacetSearchException extends Exception
{
    public FacetSearchException()
    {
    }


    public FacetSearchException(String message)
    {
        super(message);
    }


    public FacetSearchException(Throwable cause)
    {
        super(cause);
    }


    public FacetSearchException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

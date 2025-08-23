package de.hybris.platform.solrfacetsearch.suggester.exceptions;

public class SolrAutoSuggestException extends Exception
{
    public SolrAutoSuggestException(Throwable nested)
    {
        super(nested);
    }


    public SolrAutoSuggestException(String msg, Throwable nested)
    {
        super(msg, nested);
    }
}

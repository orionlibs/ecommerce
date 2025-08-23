package de.hybris.platform.solrfacetsearch.indexer.exceptions;

public class IndexerException extends Exception
{
    public IndexerException()
    {
    }


    public IndexerException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public IndexerException(String message)
    {
        super(message);
    }


    public IndexerException(Throwable cause)
    {
        super(cause);
    }
}

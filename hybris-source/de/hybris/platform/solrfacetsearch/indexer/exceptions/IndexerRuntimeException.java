package de.hybris.platform.solrfacetsearch.indexer.exceptions;

public class IndexerRuntimeException extends RuntimeException
{
    public IndexerRuntimeException()
    {
    }


    public IndexerRuntimeException(String message)
    {
        super(message);
    }


    public IndexerRuntimeException(Throwable cause)
    {
        super(cause);
    }


    public IndexerRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

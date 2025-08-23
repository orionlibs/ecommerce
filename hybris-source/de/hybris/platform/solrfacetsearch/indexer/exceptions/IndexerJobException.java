package de.hybris.platform.solrfacetsearch.indexer.exceptions;

public class IndexerJobException extends Exception
{
    public IndexerJobException()
    {
    }


    public IndexerJobException(String message)
    {
        super(message);
    }


    public IndexerJobException(Throwable cause)
    {
        super(cause);
    }


    public IndexerJobException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

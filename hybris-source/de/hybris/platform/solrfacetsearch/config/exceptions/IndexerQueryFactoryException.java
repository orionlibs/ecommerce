package de.hybris.platform.solrfacetsearch.config.exceptions;

import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerRuntimeException;

public class IndexerQueryFactoryException extends IndexerRuntimeException
{
    public IndexerQueryFactoryException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public IndexerQueryFactoryException(String message)
    {
        super(message);
    }


    public IndexerQueryFactoryException(Throwable cause)
    {
        super(cause);
    }
}

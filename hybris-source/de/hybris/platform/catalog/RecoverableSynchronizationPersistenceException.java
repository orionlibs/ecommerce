package de.hybris.platform.catalog;

import org.apache.log4j.Logger;

public class RecoverableSynchronizationPersistenceException extends RuntimeException
{
    private static final Logger LOG = Logger.getLogger(RecoverableSynchronizationPersistenceException.class);


    public RecoverableSynchronizationPersistenceException(Throwable e)
    {
        super(e);
    }
}

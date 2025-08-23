package de.hybris.platform.catalog;

import org.apache.log4j.Logger;

public class SynchronizationPersistenceException extends RuntimeException
{
    private static final Logger LOG = Logger.getLogger(SynchronizationPersistenceException.class);


    public SynchronizationPersistenceException(Throwable e)
    {
        super(e);
    }
}

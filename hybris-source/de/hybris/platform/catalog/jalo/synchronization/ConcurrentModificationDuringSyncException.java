package de.hybris.platform.catalog.jalo.synchronization;

import java.util.ConcurrentModificationException;

public class ConcurrentModificationDuringSyncException extends ConcurrentModificationException
{
    public ConcurrentModificationDuringSyncException(Throwable cause)
    {
        super(cause);
    }
}

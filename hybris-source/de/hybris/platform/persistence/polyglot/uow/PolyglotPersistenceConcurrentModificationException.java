package de.hybris.platform.persistence.polyglot.uow;

import de.hybris.platform.persistence.polyglot.PolyglotPersistenceException;

public class PolyglotPersistenceConcurrentModificationException extends PolyglotPersistenceException
{
    public PolyglotPersistenceConcurrentModificationException(String msg)
    {
        super(msg);
    }
}

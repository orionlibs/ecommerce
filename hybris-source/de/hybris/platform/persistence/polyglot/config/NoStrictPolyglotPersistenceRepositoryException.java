package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.PolyglotPersistenceException;

public class NoStrictPolyglotPersistenceRepositoryException extends PolyglotPersistenceException
{
    public NoStrictPolyglotPersistenceRepositoryException(String message)
    {
        super(message);
    }
}

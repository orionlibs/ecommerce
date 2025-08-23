package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.servicelayer.web.session.PersistedSession;

public interface SessionFilteringStrategy
{
    boolean shouldPersist(PersistedSession paramPersistedSession);
}

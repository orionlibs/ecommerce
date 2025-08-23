package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.servicelayer.web.session.PersistedSession;

public class AsyncSessionPersisterOperation
{
    private final PersistedSession sessionToUpdate;
    private final String sessionIdToRemove;


    public AsyncSessionPersisterOperation(PersistedSession sessionToUpdate)
    {
        this.sessionToUpdate = sessionToUpdate;
        this.sessionIdToRemove = null;
    }


    public AsyncSessionPersisterOperation(String sessionIdToRemove)
    {
        this.sessionIdToRemove = sessionIdToRemove;
        this.sessionToUpdate = null;
    }


    public boolean isRemoval()
    {
        return (this.sessionIdToRemove != null);
    }


    public PersistedSession getSessionToUpdate()
    {
        return this.sessionToUpdate;
    }


    public String getSessionIdToRemove()
    {
        return this.sessionIdToRemove;
    }
}

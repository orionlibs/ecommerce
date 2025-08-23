package de.hybris.platform.cms2.jalo.contents.containers;

import de.hybris.platform.jalo.SessionContext;

public abstract class AbstractCMSComponentContainer extends GeneratedAbstractCMSComponentContainer
{
    @Deprecated(since = "4.3")
    public Boolean isContainer(SessionContext ctx)
    {
        return Boolean.TRUE;
    }
}

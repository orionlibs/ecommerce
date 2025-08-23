package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.jalo.SessionContext;

public abstract class SimpleCMSComponent extends GeneratedSimpleCMSComponent
{
    @Deprecated(since = "4.3")
    public Boolean isContainer(SessionContext ctx)
    {
        return Boolean.FALSE;
    }
}

package de.hybris.platform.cms2.jalo.contents.contentslot;

import de.hybris.platform.jalo.SessionContext;

public class ContentSlot extends GeneratedContentSlot
{
    @Deprecated(since = "4.3")
    private String currentPosition;


    @Deprecated(since = "4.3")
    public String getCurrentPosition(SessionContext ctx)
    {
        return this.currentPosition;
    }


    @Deprecated(since = "4.3")
    public void setCurrentPosition(SessionContext ctx, String position)
    {
        this.currentPosition = position;
    }
}

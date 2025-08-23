package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.ItemPropertyValue;

@Deprecated(since = "ages", forRemoval = false)
public class ItemSyncDescriptor extends GeneratedItemSyncDescriptor
{
    @ForceJALO(reason = "something else")
    protected void removeLinks()
    {
    }


    public boolean isRemoval()
    {
        return (getChangedItem() == null && getTargetItem() != null);
    }


    @ForceJALO(reason = "something else")
    protected void setSequenceNumber(SessionContext ctx, int param)
    {
        super.setSequenceNumber(ctx, param);
    }


    void setTargetDirect(ItemPropertyValue targetItem)
    {
        setProperty("targetItem", targetItem);
    }
}

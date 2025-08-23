package de.hybris.platform.util;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.link.LinkOperationHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManyToManyHandler<T extends Item>
{
    private final String relationName;
    private final boolean isSource;
    private final boolean enableRestrictions;


    protected ManyToManyHandler(String relationName, boolean ownerIsSource, boolean enableRestrictions)
    {
        this.relationName = relationName;
        this.isSource = ownerIsSource;
        this.enableRestrictions = enableRestrictions;
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected ManyToManyHandler(String relationName, boolean ownerIsSource, boolean enableRestrictions, boolean partOf, boolean reOrderable)
    {
        this(relationName, ownerIsSource, enableRestrictions);
    }


    public Collection<T> getValues(SessionContext ctx, Item key)
    {
        return LinkManager.getInstance().getLinkedItems(ctx, key, this.isSource, this.relationName, null, this.enableRestrictions, 0, -1);
    }


    public void setValues(SessionContext ctx, Item key, Collection<T> values)
    {
        LinkOperationHandler op = new LinkOperationHandler(key, (values instanceof List) ? (List)values : new ArrayList<>(values), this.isSource, this.relationName, null);
        op.setReplace(true);
        op.perform(ctx);
    }
}

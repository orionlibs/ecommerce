package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;

public class ItemConflictException extends ImpExException
{
    @Deprecated(since = "ages", forRemoval = false)
    private Item existingItem;
    private PK existingItemPk;
    private final ValueLine line;


    @Deprecated(since = "ages", forRemoval = false)
    public ItemConflictException(Item existingItem, ValueLine line)
    {
        super(null, "conflict between existing item " + existingItem + " and line " + line, 0);
        this.existingItem = existingItem;
        this.line = line;
    }


    public ItemConflictException(PK existingItemPk, ValueLine line)
    {
        super(null, "conflict between existing item " + existingItemPk + " and line " + line, 0);
        this.existingItemPk = existingItemPk;
        this.line = line;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Item getExistingItem()
    {
        return this.existingItem;
    }


    public PK getExistingItemPk()
    {
        return this.existingItemPk;
    }


    public ValueLine getLine()
    {
        return this.line;
    }
}

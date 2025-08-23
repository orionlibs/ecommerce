package de.hybris.platform.retention;

import de.hybris.platform.core.PK;

public class ItemToCleanup
{
    private final PK pk;
    private final String itemType;


    private ItemToCleanup(Builder builder)
    {
        this.pk = builder.pk;
        this.itemType = builder.itemType;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public PK getPk()
    {
        return this.pk;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public String toString()
    {
        return "ItemToCleanup [pk=" + this.pk + ", itemType=" + this.itemType + "]";
    }
}

package de.hybris.platform.hmc.jalo;

import de.hybris.platform.jalo.Item;

public class ChangedItemAttributes
{
    Item item;
    String qualifier;
    Object oldValue;
    Object newValue;


    public ChangedItemAttributes(Item item, String qualifier, Object oldValue, Object newValue)
    {
        this.item = item;
        this.qualifier = qualifier;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }


    public Item getItem()
    {
        return this.item;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public Object getOldValue()
    {
        return this.oldValue;
    }


    public Object getNewValue()
    {
        return this.newValue;
    }
}

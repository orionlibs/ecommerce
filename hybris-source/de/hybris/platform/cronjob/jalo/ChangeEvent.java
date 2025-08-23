package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import java.io.Serializable;

public final class ChangeEvent implements Serializable
{
    private final ChangeDescriptor descriptor;


    public ChangeEvent(ChangeDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }


    public String getTopic()
    {
        return this.descriptor.getChangeType();
    }


    public Item getChangedItem()
    {
        return this.descriptor.getChangedItem();
    }
}

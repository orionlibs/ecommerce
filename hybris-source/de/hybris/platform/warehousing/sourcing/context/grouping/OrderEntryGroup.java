package de.hybris.platform.warehousing.sourcing.context.grouping;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import java.util.Collection;
import java.util.Collections;

public class OrderEntryGroup
{
    private final Collection<AbstractOrderEntryModel> entries;


    public OrderEntryGroup(Collection<AbstractOrderEntryModel> entries)
    {
        Preconditions.checkArgument((entries != null), "Entries cannot be null");
        this.entries = entries;
    }


    public void add(AbstractOrderEntryModel entry)
    {
        if(entry != null)
        {
            this.entries.add(entry);
        }
    }


    public void addAll(Collection<AbstractOrderEntryModel> entries)
    {
        if(entries != null)
        {
            entries.addAll(entries);
        }
    }


    public int size()
    {
        return this.entries.size();
    }


    public Collection<AbstractOrderEntryModel> getEntries()
    {
        return Collections.unmodifiableCollection(this.entries);
    }
}

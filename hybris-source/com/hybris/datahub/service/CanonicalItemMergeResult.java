package com.hybris.datahub.service;

import com.google.common.collect.Lists;
import com.hybris.datahub.model.CanonicalItem;
import java.util.List;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CanonicalItemMergeResult
{
    private final List<CanonicalItem> itemsToInsert;
    private final List<CanonicalItem> itemsToUpdate;


    private CanonicalItemMergeResult(List<CanonicalItem> itemsToInsert, List<CanonicalItem> itemsToUpdate)
    {
        this.itemsToInsert = itemsToInsert;
        this.itemsToUpdate = itemsToUpdate;
    }


    public List<CanonicalItem> getItemsToUpdate()
    {
        return this.itemsToUpdate;
    }


    public List<CanonicalItem> getItemsToInsert()
    {
        return Lists.newArrayList(this.itemsToInsert);
    }


    public String toString()
    {
        return "CanonicalItemMergeResult{itemsToInsert=" + this.itemsToInsert + ", itemsToUpdate=" + this.itemsToUpdate + "}";
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof CanonicalItemMergeResult))
        {
            return false;
        }
        CanonicalItemMergeResult that = (CanonicalItemMergeResult)o;
        if(!getItemsToInsert().equals(that.getItemsToInsert()) || ((getItemsToUpdate() != null) ?
                        !getItemsToUpdate().equals(that.getItemsToUpdate()) : (that
                        .getItemsToUpdate() != null)))
        {
            return false;
        }
    }


    public int hashCode()
    {
        int result = getItemsToInsert().hashCode();
        result = 31 * result + ((getItemsToUpdate() != null) ? getItemsToUpdate().hashCode() : 0);
        return result;
    }
}

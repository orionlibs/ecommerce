package de.hybris.platform.core;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;

public interface ItemImplCreationResult
{
    static ItemImplCreationResult existing(Item.ItemImpl impl)
    {
        return (ItemImplCreationResult)new ExistingItemImpl(impl);
    }


    static ItemImplCreationResult missing(PK pk)
    {
        return (ItemImplCreationResult)new MissingItemImpl(pk);
    }


    static ItemImplCreationResult failed(PK pk, Exception ex)
    {
        return (ItemImplCreationResult)new FailedItemImpl(pk, ex);
    }


    boolean hasItemImpl();


    JaloSystemException toJaloSystemException();


    Item.ItemImpl toItemImpl();


    boolean isFailed();
}

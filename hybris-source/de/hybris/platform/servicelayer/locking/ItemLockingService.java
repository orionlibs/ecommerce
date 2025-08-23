package de.hybris.platform.servicelayer.locking;

import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;

public interface ItemLockingService
{
    void lock(ItemModel paramItemModel);


    void lockAll(Collection<ItemModel> paramCollection);


    void unlock(ItemModel paramItemModel);


    void unlockAll(Collection<ItemModel> paramCollection);


    boolean isLocked(ItemModel paramItemModel);
}

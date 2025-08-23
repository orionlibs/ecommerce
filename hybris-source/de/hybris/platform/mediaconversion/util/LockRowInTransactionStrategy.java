package de.hybris.platform.mediaconversion.util;

import de.hybris.platform.core.model.ItemModel;

public interface LockRowInTransactionStrategy
{
    boolean lock(ItemModel paramItemModel);
}

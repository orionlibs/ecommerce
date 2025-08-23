package de.hybris.platform.retention;

import java.util.List;

public interface RetentionItemsProvider
{
    List<ItemToCleanup> nextItemsForCleanup();
}

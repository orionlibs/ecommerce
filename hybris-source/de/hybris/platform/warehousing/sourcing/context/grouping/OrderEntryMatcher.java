package de.hybris.platform.warehousing.sourcing.context.grouping;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

public interface OrderEntryMatcher<T>
{
    T getMatchingObject(AbstractOrderEntryModel paramAbstractOrderEntryModel);
}

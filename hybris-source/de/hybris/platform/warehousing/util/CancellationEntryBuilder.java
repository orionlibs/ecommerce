package de.hybris.platform.warehousing.util;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CancellationEntryBuilder
{
    public static CancellationEntryBuilder aCancellation()
    {
        return new CancellationEntryBuilder();
    }


    public List<OrderCancelEntry> build(Map<AbstractOrderEntryModel, Long> cancellationEntryInfo, CancelReason cancelReason)
    {
        List<OrderCancelEntry> cancellationEntryCollection = new ArrayList<>();
        cancellationEntryInfo.entrySet().stream().forEach(cancellation -> {
            OrderCancelEntry cancellationEntry = new OrderCancelEntry((AbstractOrderEntryModel)cancellation.getKey(), ((Long)cancellation.getValue()).longValue(), cancelReason.toString());
            cancellationEntryCollection.add(cancellationEntry);
        });
        return cancellationEntryCollection;
    }
}

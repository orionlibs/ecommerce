package de.hybris.platform.ordercancel;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderCancelRequest
{
    private final OrderModel order;
    private final List<OrderCancelEntry> entriesToCancel;
    private final boolean partialCancel;
    private final boolean partialEntryCancel;
    private String requestToken;
    private String notes;
    private CancelReason cancelReason = CancelReason.NA;


    public OrderCancelRequest(OrderModel order)
    {
        this(order, CancelReason.NA);
    }


    public OrderCancelRequest(OrderModel order, CancelReason cancelReason)
    {
        this(order, cancelReason, null);
    }


    public OrderCancelRequest(OrderModel order, CancelReason cancelReason, String notes)
    {
        this.order = order;
        List<OrderCancelEntry> tmpList = new ArrayList<>();
        for(AbstractOrderEntryModel aoem : order.getEntries())
        {
            tmpList.add(new OrderCancelEntry(aoem, aoem.getQuantity().longValue()));
        }
        Collections.sort(tmpList, (order1, order2) -> order1.getOrderEntry().getEntryNumber().compareTo(order2.getOrderEntry().getEntryNumber()));
        this.entriesToCancel = Collections.unmodifiableList(tmpList);
        this.partialCancel = false;
        this.partialEntryCancel = false;
        this.cancelReason = cancelReason;
        this.notes = notes;
    }


    public OrderCancelRequest(OrderModel order, List<OrderCancelEntry> orderCancelEntries)
    {
        this(order, orderCancelEntries, null);
    }


    public OrderCancelRequest(OrderModel order, List<OrderCancelEntry> orderCancelEntries, String notes)
    {
        this.order = order;
        if(orderCancelEntries == null || orderCancelEntries.isEmpty())
        {
            throw new IllegalArgumentException("orderCancelEntries is null or empty");
        }
        Map<Integer, OrderCancelEntry> cancelEntriesMap = new HashMap<>();
        boolean partialEntryCancelDetected = false;
        for(OrderCancelEntry oce : orderCancelEntries)
        {
            if(!order.equals(oce.getOrderEntry().getOrder()))
            {
                throw new IllegalArgumentException("Attempt to add Order Entry that belongs to another order");
            }
            if(cancelEntriesMap.containsKey(oce.getOrderEntry().getEntryNumber()))
            {
                throw new IllegalArgumentException("Attempt to add Order Entry twice");
            }
            cancelEntriesMap.put(oce.getOrderEntry().getEntryNumber(), oce);
            if(oce.getCancelQuantity() < oce.getOrderEntry().getQuantity().longValue())
            {
                partialEntryCancelDetected = true;
            }
        }
        List<OrderCancelEntry> tmpList = new ArrayList<>(cancelEntriesMap.values());
        Collections.sort(tmpList, (oce1, oce2) -> oce1.getOrderEntry().getEntryNumber().compareTo(oce2.getOrderEntry().getEntryNumber()));
        this.entriesToCancel = Collections.unmodifiableList(tmpList);
        this.partialEntryCancel = partialEntryCancelDetected;
        if(partialEntryCancelDetected)
        {
            this.partialCancel = true;
        }
        else
        {
            boolean allOrderEntriesCancelled = true;
            for(AbstractOrderEntryModel aoem : order.getEntries())
            {
                if(!cancelEntriesMap.containsKey(aoem.getEntryNumber()))
                {
                    allOrderEntriesCancelled = false;
                }
            }
            this.partialCancel = !allOrderEntriesCancelled;
        }
        this.notes = notes;
    }


    public OrderModel getOrder()
    {
        return this.order;
    }


    public List<OrderCancelEntry> getEntriesToCancel()
    {
        return this.entriesToCancel;
    }


    public boolean isPartialCancel()
    {
        return this.partialCancel;
    }


    public boolean isPartialEntryCancel()
    {
        return this.partialEntryCancel;
    }


    public String getRequestToken()
    {
        return this.requestToken;
    }


    public void setRequestToken(String requestToken)
    {
        this.requestToken = requestToken;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    public CancelReason getCancelReason()
    {
        return this.cancelReason;
    }


    public void setCancelReason(CancelReason cancelReason)
    {
        this.cancelReason = cancelReason;
    }
}

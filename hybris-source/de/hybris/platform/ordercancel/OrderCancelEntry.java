package de.hybris.platform.ordercancel;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

public class OrderCancelEntry
{
    private final AbstractOrderEntryModel orderEntry;
    private final long cancelQuantity;
    private String notes;
    private CancelReason cancelReason;


    public OrderCancelEntry(AbstractOrderEntryModel orderEntry)
    {
        this.orderEntry = orderEntry;
        this.cancelQuantity = orderEntry.getQuantity().longValue();
    }


    public OrderCancelEntry(AbstractOrderEntryModel orderEntry, long cancelQuantity)
    {
        this(orderEntry, cancelQuantity, null);
    }


    public OrderCancelEntry(AbstractOrderEntryModel orderEntry, long cancelQuantity, String notes)
    {
        this(orderEntry, cancelQuantity, notes, CancelReason.NA);
    }


    public OrderCancelEntry(AbstractOrderEntryModel orderEntry, String notes, CancelReason cancelReason)
    {
        this(orderEntry, orderEntry.getQuantity().longValue(), notes, cancelReason);
    }


    public OrderCancelEntry(AbstractOrderEntryModel orderEntry, long cancelQuantity, String notes, CancelReason cancelReason)
    {
        this.orderEntry = orderEntry;
        if(cancelQuantity < 0L)
        {
            throw new IllegalArgumentException("OrderCancelEntry's cancelQuantity value must be greater than zero");
        }
        if(cancelQuantity > orderEntry.getQuantity().longValue())
        {
            throw new IllegalArgumentException("OrderCancelEntry's cancelQuantity value cannot be greater than actual OrderEntry quantity");
        }
        this.cancelQuantity = cancelQuantity;
        this.notes = notes;
        this.cancelReason = cancelReason;
    }


    public AbstractOrderEntryModel getOrderEntry()
    {
        return this.orderEntry;
    }


    public long getCancelQuantity()
    {
        return this.cancelQuantity;
    }


    public String getNotes()
    {
        return this.notes;
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

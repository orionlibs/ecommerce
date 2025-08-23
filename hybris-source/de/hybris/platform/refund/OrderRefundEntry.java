package de.hybris.platform.refund;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

public class OrderRefundEntry
{
    private final AbstractOrderEntryModel orderEntry;
    private final long expectedQuantity;
    private final String notes;
    private RefundReason reason;


    public OrderRefundEntry(AbstractOrderEntryModel orderEntry, long expectedQuantity)
    {
        this(orderEntry, expectedQuantity, null);
    }


    public OrderRefundEntry(AbstractOrderEntryModel orderEntry, long expectedQuantity, String notes)
    {
        this(orderEntry, expectedQuantity, notes, null);
    }


    public OrderRefundEntry(AbstractOrderEntryModel orderEntry, long expectedQuantity, String notes, RefundReason reason)
    {
        this.orderEntry = orderEntry;
        if(expectedQuantity <= 0L)
        {
            throw new IllegalArgumentException("OrderRefundEntry's expectedQuantity value must be greater than zero");
        }
        if(expectedQuantity > orderEntry.getQuantity().longValue())
        {
            throw new IllegalArgumentException("OrderRefundEntry's expectedQuantity value cannot be greater than actual OrderEntry quantity");
        }
        this.expectedQuantity = expectedQuantity;
        this.notes = notes;
        this.reason = reason;
    }


    public AbstractOrderEntryModel getOrderEntry()
    {
        return this.orderEntry;
    }


    public long getExpectedQuantity()
    {
        return this.expectedQuantity;
    }


    public String getNotes()
    {
        return this.notes;
    }


    public RefundReason getRefundReason()
    {
        return this.reason;
    }


    public void setRefundReason(RefundReason reason)
    {
        this.reason = reason;
    }
}

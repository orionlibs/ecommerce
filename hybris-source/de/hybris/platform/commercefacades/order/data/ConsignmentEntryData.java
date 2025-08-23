package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.List;

public class ConsignmentEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private OrderEntryData orderEntry;
    private Long quantity;
    private Long shippedQuantity;
    private List<ConsignmentEntryData> otherVariants;


    public void setOrderEntry(OrderEntryData orderEntry)
    {
        this.orderEntry = orderEntry;
    }


    public OrderEntryData getOrderEntry()
    {
        return this.orderEntry;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }


    public void setShippedQuantity(Long shippedQuantity)
    {
        this.shippedQuantity = shippedQuantity;
    }


    public Long getShippedQuantity()
    {
        return this.shippedQuantity;
    }


    public void setOtherVariants(List<ConsignmentEntryData> otherVariants)
    {
        this.otherVariants = otherVariants;
    }


    public List<ConsignmentEntryData> getOtherVariants()
    {
        return this.otherVariants;
    }
}

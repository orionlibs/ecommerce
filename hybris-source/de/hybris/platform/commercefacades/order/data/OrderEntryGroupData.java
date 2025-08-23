package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commercefacades.product.data.PriceData;
import java.io.Serializable;
import java.util.Collection;

public class OrderEntryGroupData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private PriceData totalPriceWithTax;
    private Collection<OrderEntryData> entries;
    private Long quantity;


    public void setTotalPriceWithTax(PriceData totalPriceWithTax)
    {
        this.totalPriceWithTax = totalPriceWithTax;
    }


    public PriceData getTotalPriceWithTax()
    {
        return this.totalPriceWithTax;
    }


    public void setEntries(Collection<OrderEntryData> entries)
    {
        this.entries = entries;
    }


    public Collection<OrderEntryData> getEntries()
    {
        return this.entries;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }
}

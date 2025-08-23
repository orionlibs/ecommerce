package de.hybris.platform.commercewebservices.core.order.data;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import java.io.Serializable;
import java.util.List;

public class OrderEntryDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<OrderEntryData> orderEntries;


    public void setOrderEntries(List<OrderEntryData> orderEntries)
    {
        this.orderEntries = orderEntries;
    }


    public List<OrderEntryData> getOrderEntries()
    {
        return this.orderEntries;
    }
}

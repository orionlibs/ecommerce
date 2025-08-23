package de.hybris.platform.ordermanagementfacades.cancellation.data;

import java.io.Serializable;
import java.util.List;

public class OrderCancelRequestData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderCode;
    private List<OrderCancelEntryData> entries;
    private String userId;


    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }


    public void setEntries(List<OrderCancelEntryData> entries)
    {
        this.entries = entries;
    }


    public List<OrderCancelEntryData> getEntries()
    {
        return this.entries;
    }


    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    public String getUserId()
    {
        return this.userId;
    }
}

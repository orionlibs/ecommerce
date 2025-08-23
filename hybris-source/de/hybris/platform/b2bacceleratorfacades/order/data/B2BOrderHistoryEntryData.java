package de.hybris.platform.b2bacceleratorfacades.order.data;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import java.io.Serializable;
import java.util.Date;

public class B2BOrderHistoryEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private OrderData b2bOrderData;
    private String description;
    private Date timeStamp;
    private OrderData previousOrderVersionData;
    private PrincipalData ownerData;


    public void setB2bOrderData(OrderData b2bOrderData)
    {
        this.b2bOrderData = b2bOrderData;
    }


    public OrderData getB2bOrderData()
    {
        return this.b2bOrderData;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
    }


    public Date getTimeStamp()
    {
        return this.timeStamp;
    }


    public void setPreviousOrderVersionData(OrderData previousOrderVersionData)
    {
        this.previousOrderVersionData = previousOrderVersionData;
    }


    public OrderData getPreviousOrderVersionData()
    {
        return this.previousOrderVersionData;
    }


    public void setOwnerData(PrincipalData ownerData)
    {
        this.ownerData = ownerData;
    }


    public PrincipalData getOwnerData()
    {
        return this.ownerData;
    }
}

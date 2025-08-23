package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;

public class HeavyOrderQuestionsCartData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Boolean largeTruckEntry;
    private Boolean liftAvailable;
    private Boolean loadingDock;
    private Boolean orderDeliveredInside;
    private String name;
    private String email;
    private String phoneNumber;
    private String truckSize;


    public void setLargeTruckEntry(Boolean largeTruckEntry)
    {
        this.largeTruckEntry = largeTruckEntry;
    }


    public Boolean getLargeTruckEntry()
    {
        return this.largeTruckEntry;
    }


    public void setLiftAvailable(Boolean liftAvailable)
    {
        this.liftAvailable = liftAvailable;
    }


    public Boolean getLiftAvailable()
    {
        return this.liftAvailable;
    }


    public void setLoadingDock(Boolean loadingDock)
    {
        this.loadingDock = loadingDock;
    }


    public Boolean getLoadingDock()
    {
        return this.loadingDock;
    }


    public void setOrderDeliveredInside(Boolean orderDeliveredInside)
    {
        this.orderDeliveredInside = orderDeliveredInside;
    }


    public Boolean getOrderDeliveredInside()
    {
        return this.orderDeliveredInside;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }


    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }


    public void setTruckSize(String truckSize)
    {
        this.truckSize = truckSize;
    }


    public String getTruckSize()
    {
        return this.truckSize;
    }
}

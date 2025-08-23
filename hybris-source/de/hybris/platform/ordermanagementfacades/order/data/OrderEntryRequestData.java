package de.hybris.platform.ordermanagementfacades.order.data;

import java.io.Serializable;

public class OrderEntryRequestData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer entryNumber;
    private long quantity;
    private String unitCode;
    private String productCode;
    private String deliveryModeCode;
    private String deliveryPointOfService;
    private double basePrice;
    private double totalPrice;
    private String pointOfService;


    public void setEntryNumber(Integer entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public Integer getEntryNumber()
    {
        return this.entryNumber;
    }


    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }


    public long getQuantity()
    {
        return this.quantity;
    }


    public void setUnitCode(String unitCode)
    {
        this.unitCode = unitCode;
    }


    public String getUnitCode()
    {
        return this.unitCode;
    }


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setDeliveryModeCode(String deliveryModeCode)
    {
        this.deliveryModeCode = deliveryModeCode;
    }


    public String getDeliveryModeCode()
    {
        return this.deliveryModeCode;
    }


    public void setDeliveryPointOfService(String deliveryPointOfService)
    {
        this.deliveryPointOfService = deliveryPointOfService;
    }


    public String getDeliveryPointOfService()
    {
        return this.deliveryPointOfService;
    }


    public void setBasePrice(double basePrice)
    {
        this.basePrice = basePrice;
    }


    public double getBasePrice()
    {
        return this.basePrice;
    }


    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public double getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setPointOfService(String pointOfService)
    {
        this.pointOfService = pointOfService;
    }


    public String getPointOfService()
    {
        return this.pointOfService;
    }
}

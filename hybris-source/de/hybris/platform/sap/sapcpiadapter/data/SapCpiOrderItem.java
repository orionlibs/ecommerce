package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiOrderItem")
public class SapCpiOrderItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String entryNumber;
    private String quantity;
    private String currencyIsoCode;
    private String unit;
    private String productCode;
    private String productName;
    private String plant;
    private String namedDeliveryDate;
    private String itemCategory;


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public String getOrderId()
    {
        return this.orderId;
    }


    public void setEntryNumber(String entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public String getEntryNumber()
    {
        return this.entryNumber;
    }


    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }


    public String getQuantity()
    {
        return this.quantity;
    }


    public void setCurrencyIsoCode(String currencyIsoCode)
    {
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public void setUnit(String unit)
    {
        this.unit = unit;
    }


    public String getUnit()
    {
        return this.unit;
    }


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setProductName(String productName)
    {
        this.productName = productName;
    }


    public String getProductName()
    {
        return this.productName;
    }


    public void setPlant(String plant)
    {
        this.plant = plant;
    }


    public String getPlant()
    {
        return this.plant;
    }


    public void setNamedDeliveryDate(String namedDeliveryDate)
    {
        this.namedDeliveryDate = namedDeliveryDate;
    }


    public String getNamedDeliveryDate()
    {
        return this.namedDeliveryDate;
    }


    public void setItemCategory(String itemCategory)
    {
        this.itemCategory = itemCategory;
    }


    public String getItemCategory()
    {
        return this.itemCategory;
    }
}

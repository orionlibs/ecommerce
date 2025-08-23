package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiOrderPriceComponent")
public class SapCpiOrderPriceComponent implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String entryNumber;
    private String value;
    private String unit;
    private String absolute;
    private String conditionCode;
    private String conditionCounter;
    private String currencyIsoCode;
    private String priceQuantity;


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


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public void setUnit(String unit)
    {
        this.unit = unit;
    }


    public String getUnit()
    {
        return this.unit;
    }


    public void setAbsolute(String absolute)
    {
        this.absolute = absolute;
    }


    public String getAbsolute()
    {
        return this.absolute;
    }


    public void setConditionCode(String conditionCode)
    {
        this.conditionCode = conditionCode;
    }


    public String getConditionCode()
    {
        return this.conditionCode;
    }


    public void setConditionCounter(String conditionCounter)
    {
        this.conditionCounter = conditionCounter;
    }


    public String getConditionCounter()
    {
        return this.conditionCounter;
    }


    public void setCurrencyIsoCode(String currencyIsoCode)
    {
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public void setPriceQuantity(String priceQuantity)
    {
        this.priceQuantity = priceQuantity;
    }


    public String getPriceQuantity()
    {
        return this.priceQuantity;
    }
}

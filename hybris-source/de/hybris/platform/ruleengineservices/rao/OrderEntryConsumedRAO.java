package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class OrderEntryConsumedRAO implements Serializable
{
    private String firedRuleCode;
    private OrderEntryRAO orderEntry;
    private int quantity;
    private BigDecimal adjustedUnitPrice;


    public void setFiredRuleCode(String firedRuleCode)
    {
        this.firedRuleCode = firedRuleCode;
    }


    public String getFiredRuleCode()
    {
        return this.firedRuleCode;
    }


    public void setOrderEntry(OrderEntryRAO orderEntry)
    {
        this.orderEntry = orderEntry;
    }


    public OrderEntryRAO getOrderEntry()
    {
        return this.orderEntry;
    }


    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }


    public int getQuantity()
    {
        return this.quantity;
    }


    public void setAdjustedUnitPrice(BigDecimal adjustedUnitPrice)
    {
        this.adjustedUnitPrice = adjustedUnitPrice;
    }


    public BigDecimal getAdjustedUnitPrice()
    {
        return this.adjustedUnitPrice;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        OrderEntryConsumedRAO other = (OrderEntryConsumedRAO)o;
        return (Objects.equals(getFiredRuleCode(), other.getFiredRuleCode()) &&
                        Objects.equals(getOrderEntry(), other.getOrderEntry()) &&
                        Objects.equals(Integer.valueOf(getQuantity()), Integer.valueOf(other.getQuantity())) &&
                        Objects.equals(getAdjustedUnitPrice(), other.getAdjustedUnitPrice()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.firedRuleCode;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.orderEntry;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = Integer.valueOf(this.quantity);
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.adjustedUnitPrice;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}

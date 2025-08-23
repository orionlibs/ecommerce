package de.hybris.platform.ruleengineservices.rao;

import de.hybris.platform.ruleengineservices.enums.FixedDiscountDistributeStrategy;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class EntriesDiscountDistributeStrategyRPD implements Serializable
{
    private List<OrderEntryRAO> orderEntries;
    private BigDecimal totalDiscount;
    private String currencyIsoCode;
    private boolean fixDiscount;
    private FixedDiscountDistributeStrategy fixedDiscountDistributeStrategy;


    public void setOrderEntries(List<OrderEntryRAO> orderEntries)
    {
        this.orderEntries = orderEntries;
    }


    public List<OrderEntryRAO> getOrderEntries()
    {
        return this.orderEntries;
    }


    public void setTotalDiscount(BigDecimal totalDiscount)
    {
        this.totalDiscount = totalDiscount;
    }


    public BigDecimal getTotalDiscount()
    {
        return this.totalDiscount;
    }


    public void setCurrencyIsoCode(String currencyIsoCode)
    {
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public void setFixDiscount(boolean fixDiscount)
    {
        this.fixDiscount = fixDiscount;
    }


    public boolean isFixDiscount()
    {
        return this.fixDiscount;
    }


    public void setFixedDiscountDistributeStrategy(FixedDiscountDistributeStrategy fixedDiscountDistributeStrategy)
    {
        this.fixedDiscountDistributeStrategy = fixedDiscountDistributeStrategy;
    }


    public FixedDiscountDistributeStrategy getFixedDiscountDistributeStrategy()
    {
        return this.fixedDiscountDistributeStrategy;
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
        EntriesDiscountDistributeStrategyRPD other = (EntriesDiscountDistributeStrategyRPD)o;
        return (Objects.equals(getOrderEntries(), other.getOrderEntries()) &&
                        Objects.equals(getTotalDiscount(), other.getTotalDiscount()) &&
                        Objects.equals(getCurrencyIsoCode(), other.getCurrencyIsoCode()) &&
                        Objects.equals(Boolean.valueOf(isFixDiscount()), Boolean.valueOf(other.isFixDiscount())) &&
                        Objects.equals(getFixedDiscountDistributeStrategy(), other.getFixedDiscountDistributeStrategy()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<OrderEntryRAO> attribute = (Object<OrderEntryRAO>)this.orderEntries;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<OrderEntryRAO>)this.totalDiscount;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<OrderEntryRAO>)this.currencyIsoCode;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<OrderEntryRAO>)Boolean.valueOf(this.fixDiscount);
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        FixedDiscountDistributeStrategy fixedDiscountDistributeStrategy = this.fixedDiscountDistributeStrategy;
        result = 31 * result + ((fixedDiscountDistributeStrategy == null) ? 0 : fixedDiscountDistributeStrategy.hashCode());
        return result;
    }
}

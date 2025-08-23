package de.hybris.platform.ruleengineservices.rao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AbstractOrderRAO extends AbstractActionedRAO
{
    private String code;
    private BigDecimal total;
    private BigDecimal totalIncludingCharges;
    private BigDecimal subTotal;
    private BigDecimal deliveryCost;
    private BigDecimal paymentCost;
    private String currencyIsoCode;
    private Set<OrderEntryRAO> entries;
    private List<DiscountValueRAO> discountValues;
    private UserRAO user;
    private PaymentModeRAO paymentMode;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setTotal(BigDecimal total)
    {
        this.total = total;
    }


    public BigDecimal getTotal()
    {
        return this.total;
    }


    public void setTotalIncludingCharges(BigDecimal totalIncludingCharges)
    {
        this.totalIncludingCharges = totalIncludingCharges;
    }


    public BigDecimal getTotalIncludingCharges()
    {
        return this.totalIncludingCharges;
    }


    public void setSubTotal(BigDecimal subTotal)
    {
        this.subTotal = subTotal;
    }


    public BigDecimal getSubTotal()
    {
        return this.subTotal;
    }


    public void setDeliveryCost(BigDecimal deliveryCost)
    {
        this.deliveryCost = deliveryCost;
    }


    public BigDecimal getDeliveryCost()
    {
        return this.deliveryCost;
    }


    public void setPaymentCost(BigDecimal paymentCost)
    {
        this.paymentCost = paymentCost;
    }


    public BigDecimal getPaymentCost()
    {
        return this.paymentCost;
    }


    public void setCurrencyIsoCode(String currencyIsoCode)
    {
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public void setEntries(Set<OrderEntryRAO> entries)
    {
        this.entries = entries;
    }


    public Set<OrderEntryRAO> getEntries()
    {
        return this.entries;
    }


    public void setDiscountValues(List<DiscountValueRAO> discountValues)
    {
        this.discountValues = discountValues;
    }


    public List<DiscountValueRAO> getDiscountValues()
    {
        return this.discountValues;
    }


    public void setUser(UserRAO user)
    {
        this.user = user;
    }


    public UserRAO getUser()
    {
        return this.user;
    }


    public void setPaymentMode(PaymentModeRAO paymentMode)
    {
        this.paymentMode = paymentMode;
    }


    public PaymentModeRAO getPaymentMode()
    {
        return this.paymentMode;
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
        AbstractOrderRAO other = (AbstractOrderRAO)o;
        return Objects.equals(getCode(), other.getCode());
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.code;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}

package de.hybris.platform.ruleengineservices.rao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OrderEntryRAO extends AbstractActionedRAO
{
    private Integer entryNumber;
    private int quantity;
    private BigDecimal basePrice;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String currencyIsoCode;
    private AbstractOrderRAO order;
    private List<DiscountValueRAO> discountValues;
    private List<Integer> entryGroupNumbers;
    private boolean giveAway;
    private String productCode;
    private Set<String> categoryCodes;
    private Set<String> baseProductCodes;
    private int availableQuantity;


    public void setEntryNumber(Integer entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public Integer getEntryNumber()
    {
        return this.entryNumber;
    }


    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }


    public int getQuantity()
    {
        return this.quantity;
    }


    public void setBasePrice(BigDecimal basePrice)
    {
        this.basePrice = basePrice;
    }


    public BigDecimal getBasePrice()
    {
        return this.basePrice;
    }


    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }


    public BigDecimal getPrice()
    {
        return this.price;
    }


    public void setTotalPrice(BigDecimal totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public BigDecimal getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setCurrencyIsoCode(String currencyIsoCode)
    {
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public void setOrder(AbstractOrderRAO order)
    {
        this.order = order;
    }


    public AbstractOrderRAO getOrder()
    {
        return this.order;
    }


    public void setDiscountValues(List<DiscountValueRAO> discountValues)
    {
        this.discountValues = discountValues;
    }


    public List<DiscountValueRAO> getDiscountValues()
    {
        return this.discountValues;
    }


    public void setEntryGroupNumbers(List<Integer> entryGroupNumbers)
    {
        this.entryGroupNumbers = entryGroupNumbers;
    }


    public List<Integer> getEntryGroupNumbers()
    {
        return this.entryGroupNumbers;
    }


    public void setGiveAway(boolean giveAway)
    {
        this.giveAway = giveAway;
    }


    public boolean isGiveAway()
    {
        return this.giveAway;
    }


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setCategoryCodes(Set<String> categoryCodes)
    {
        this.categoryCodes = categoryCodes;
    }


    public Set<String> getCategoryCodes()
    {
        return this.categoryCodes;
    }


    public void setBaseProductCodes(Set<String> baseProductCodes)
    {
        this.baseProductCodes = baseProductCodes;
    }


    public Set<String> getBaseProductCodes()
    {
        return this.baseProductCodes;
    }


    public void setAvailableQuantity(int availableQuantity)
    {
        this.availableQuantity = availableQuantity;
    }


    public int getAvailableQuantity()
    {
        return this.availableQuantity;
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
        OrderEntryRAO other = (OrderEntryRAO)o;
        return (Objects.equals(getEntryNumber(), other.getEntryNumber()) &&
                        Objects.equals(getOrder(), other.getOrder()) &&
                        Objects.equals(getProductCode(), other.getProductCode()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.entryNumber;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.order;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.productCode;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}

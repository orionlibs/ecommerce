package de.hybris.platform.commercefacades.quote.data;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.enums.QuoteState;
import java.util.Date;

public class QuoteData extends AbstractOrderData
{
    private Integer version;
    private QuoteState state;
    private Date creationTime;
    private Date updatedTime;
    private Boolean hasCart;
    private String orderCode;
    private PriceData previousEstimatedTotal;


    public void setVersion(Integer version)
    {
        this.version = version;
    }


    public Integer getVersion()
    {
        return this.version;
    }


    public void setState(QuoteState state)
    {
        this.state = state;
    }


    public QuoteState getState()
    {
        return this.state;
    }


    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    public Date getCreationTime()
    {
        return this.creationTime;
    }


    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }


    public Date getUpdatedTime()
    {
        return this.updatedTime;
    }


    public void setHasCart(Boolean hasCart)
    {
        this.hasCart = hasCart;
    }


    public Boolean getHasCart()
    {
        return this.hasCart;
    }


    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }


    public String getOrderCode()
    {
        return this.orderCode;
    }


    public void setPreviousEstimatedTotal(PriceData previousEstimatedTotal)
    {
        this.previousEstimatedTotal = previousEstimatedTotal;
    }


    public PriceData getPreviousEstimatedTotal()
    {
        return this.previousEstimatedTotal;
    }
}

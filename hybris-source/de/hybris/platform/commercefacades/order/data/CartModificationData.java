package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.Set;

public class CartModificationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String statusCode;
    private long quantityAdded;
    private long quantity;
    private OrderEntryData entry;
    private Boolean deliveryModeChanged;
    private String cartCode;
    private String statusMessage;
    private Set<Integer> entryGroupNumbers;


    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }


    public String getStatusCode()
    {
        return this.statusCode;
    }


    public void setQuantityAdded(long quantityAdded)
    {
        this.quantityAdded = quantityAdded;
    }


    public long getQuantityAdded()
    {
        return this.quantityAdded;
    }


    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }


    public long getQuantity()
    {
        return this.quantity;
    }


    public void setEntry(OrderEntryData entry)
    {
        this.entry = entry;
    }


    public OrderEntryData getEntry()
    {
        return this.entry;
    }


    public void setDeliveryModeChanged(Boolean deliveryModeChanged)
    {
        this.deliveryModeChanged = deliveryModeChanged;
    }


    public Boolean getDeliveryModeChanged()
    {
        return this.deliveryModeChanged;
    }


    public void setCartCode(String cartCode)
    {
        this.cartCode = cartCode;
    }


    public String getCartCode()
    {
        return this.cartCode;
    }


    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }


    public String getStatusMessage()
    {
        return this.statusMessage;
    }


    public void setEntryGroupNumbers(Set<Integer> entryGroupNumbers)
    {
        this.entryGroupNumbers = entryGroupNumbers;
    }


    public Set<Integer> getEntryGroupNumbers()
    {
        return this.entryGroupNumbers;
    }
}

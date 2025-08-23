package de.hybris.platform.commerceservices.order;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.io.Serializable;
import java.util.Set;

public class CommerceCartModification implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String statusCode;
    private long quantity;
    private long quantityAdded;
    private AbstractOrderEntryModel entry;
    private ProductModel product;
    private Boolean deliveryModeChanged;
    private Set<Integer> entryGroupNumbers;


    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }


    public String getStatusCode()
    {
        return this.statusCode;
    }


    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }


    public long getQuantity()
    {
        return this.quantity;
    }


    public void setQuantityAdded(long quantityAdded)
    {
        this.quantityAdded = quantityAdded;
    }


    public long getQuantityAdded()
    {
        return this.quantityAdded;
    }


    public void setEntry(AbstractOrderEntryModel entry)
    {
        this.entry = entry;
    }


    public AbstractOrderEntryModel getEntry()
    {
        return this.entry;
    }


    public void setProduct(ProductModel product)
    {
        this.product = product;
    }


    public ProductModel getProduct()
    {
        return this.product;
    }


    public void setDeliveryModeChanged(Boolean deliveryModeChanged)
    {
        this.deliveryModeChanged = deliveryModeChanged;
    }


    public Boolean getDeliveryModeChanged()
    {
        return this.deliveryModeChanged;
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

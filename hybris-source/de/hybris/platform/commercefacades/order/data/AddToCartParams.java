package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.Set;

public class AddToCartParams implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String productCode;
    private long quantity;
    private String storeId;
    private Set<Integer> entryGroupNumbers;


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }


    public long getQuantity()
    {
        return this.quantity;
    }


    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }


    public String getStoreId()
    {
        return this.storeId;
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

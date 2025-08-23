package de.hybris.platform.b2bacceleratorfacades.product.data;

import java.io.Serializable;

public class CartEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long entryNumber;
    private String sku;
    private Long quantity;


    public void setEntryNumber(Long entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public Long getEntryNumber()
    {
        return this.entryNumber;
    }


    public void setSku(String sku)
    {
        this.sku = sku;
    }


    public String getSku()
    {
        return this.sku;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }
}

package de.hybris.platform.b2bacceleratorfacades.product.data;

import java.io.Serializable;

public class ProductQuantityData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String sku;
    private Integer quantity;


    public void setSku(String sku)
    {
        this.sku = sku;
    }


    public String getSku()
    {
        return this.sku;
    }


    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }


    public Integer getQuantity()
    {
        return this.quantity;
    }
}

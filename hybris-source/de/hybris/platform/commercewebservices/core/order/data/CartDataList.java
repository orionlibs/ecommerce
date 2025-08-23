package de.hybris.platform.commercewebservices.core.order.data;

import de.hybris.platform.commercefacades.order.data.CartData;
import java.io.Serializable;
import java.util.List;

public class CartDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CartData> carts;


    public void setCarts(List<CartData> carts)
    {
        this.carts = carts;
    }


    public List<CartData> getCarts()
    {
        return this.carts;
    }
}

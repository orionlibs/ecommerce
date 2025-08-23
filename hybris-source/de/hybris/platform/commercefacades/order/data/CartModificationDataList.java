package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.List;

public class CartModificationDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CartModificationData> cartModificationList;


    public void setCartModificationList(List<CartModificationData> cartModificationList)
    {
        this.cartModificationList = cartModificationList;
    }


    public List<CartModificationData> getCartModificationList()
    {
        return this.cartModificationList;
    }
}

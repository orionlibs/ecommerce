package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import java.util.List;

public class CartModificationListData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CartModificationData> cartModifications;


    public void setCartModifications(List<CartModificationData> cartModifications)
    {
        this.cartModifications = cartModifications;
    }


    public List<CartModificationData> getCartModifications()
    {
        return this.cartModifications;
    }
}

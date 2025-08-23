package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.core.model.order.CartModel;
import java.io.Serializable;

public class CommerceSaveCartResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CartModel savedCart;


    public void setSavedCart(CartModel savedCart)
    {
        this.savedCart = savedCart;
    }


    public CartModel getSavedCart()
    {
        return this.savedCart;
    }
}

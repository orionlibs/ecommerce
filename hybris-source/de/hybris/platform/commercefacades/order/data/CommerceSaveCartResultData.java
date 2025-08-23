package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;

public class CommerceSaveCartResultData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CartData savedCartData;


    public void setSavedCartData(CartData savedCartData)
    {
        this.savedCartData = savedCartData;
    }


    public CartData getSavedCartData()
    {
        return this.savedCartData;
    }
}

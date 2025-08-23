package de.hybris.platform.acceleratorfacades.cart.data;

import de.hybris.platform.commercefacades.order.data.CartData;
import java.io.Serializable;

public class CartWrapperData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CartData cartData;
    private String errorMsg;
    private String successMsg;


    public void setCartData(CartData cartData)
    {
        this.cartData = cartData;
    }


    public CartData getCartData()
    {
        return this.cartData;
    }


    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }


    public String getErrorMsg()
    {
        return this.errorMsg;
    }


    public void setSuccessMsg(String successMsg)
    {
        this.successMsg = successMsg;
    }


    public String getSuccessMsg()
    {
        return this.successMsg;
    }
}

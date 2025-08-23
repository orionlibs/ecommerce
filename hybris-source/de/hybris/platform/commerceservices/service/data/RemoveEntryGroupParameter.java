package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.core.model.order.CartModel;
import java.io.Serializable;

public class RemoveEntryGroupParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CartModel cart;
    private boolean enableHooks;
    private Integer entryGroupNumber;


    public void setCart(CartModel cart)
    {
        this.cart = cart;
    }


    public CartModel getCart()
    {
        return this.cart;
    }


    public void setEnableHooks(boolean enableHooks)
    {
        this.enableHooks = enableHooks;
    }


    public boolean isEnableHooks()
    {
        return this.enableHooks;
    }


    public void setEntryGroupNumber(Integer entryGroupNumber)
    {
        this.entryGroupNumber = entryGroupNumber;
    }


    public Integer getEntryGroupNumber()
    {
        return this.entryGroupNumber;
    }
}

package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.core.model.order.CartModel;
import java.io.Serializable;

public class CommerceSaveCartParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CartModel cart;
    private String name;
    private String description;
    private boolean enableHooks;


    public void setCart(CartModel cart)
    {
        this.cart = cart;
    }


    public CartModel getCart()
    {
        return this.cart;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setEnableHooks(boolean enableHooks)
    {
        this.enableHooks = enableHooks;
    }


    public boolean isEnableHooks()
    {
        return this.enableHooks;
    }
}

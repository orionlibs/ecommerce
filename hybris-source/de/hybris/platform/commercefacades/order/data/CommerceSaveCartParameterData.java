package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;

public class CommerceSaveCartParameterData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String cartId;
    private String name;
    private String description;
    private boolean enableHooks;


    public void setCartId(String cartId)
    {
        this.cartId = cartId;
    }


    public String getCartId()
    {
        return this.cartId;
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

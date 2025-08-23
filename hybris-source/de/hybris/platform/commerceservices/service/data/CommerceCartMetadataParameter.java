package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.core.model.order.CartModel;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class CommerceCartMetadataParameter implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Optional<String> name;
    private Optional<String> description;
    private Optional<Date> expirationTime;
    private boolean removeExpirationTime;
    private CartModel cart;
    private boolean enableHooks;


    public void setName(Optional<String> name)
    {
        this.name = name;
    }


    public Optional<String> getName()
    {
        return this.name;
    }


    public void setDescription(Optional<String> description)
    {
        this.description = description;
    }


    public Optional<String> getDescription()
    {
        return this.description;
    }


    public void setExpirationTime(Optional<Date> expirationTime)
    {
        this.expirationTime = expirationTime;
    }


    public Optional<Date> getExpirationTime()
    {
        return this.expirationTime;
    }


    public void setRemoveExpirationTime(boolean removeExpirationTime)
    {
        this.removeExpirationTime = removeExpirationTime;
    }


    public boolean isRemoveExpirationTime()
    {
        return this.removeExpirationTime;
    }


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
}

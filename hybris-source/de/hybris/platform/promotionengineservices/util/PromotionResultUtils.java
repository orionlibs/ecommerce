package de.hybris.platform.promotionengineservices.util;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.model.PromotionResultModel;
import org.springframework.beans.factory.annotation.Required;

public class PromotionResultUtils
{
    private CartService cartService;


    public AbstractOrderModel getOrder(PromotionResultModel promotionResult)
    {
        if(getCartService().hasSessionCart() &&
                        getCartService().getSessionCart().getCode().equals(promotionResult.getOrderCode()))
        {
            return (AbstractOrderModel)getCartService().getSessionCart();
        }
        if(promotionResult.getOrder() != null)
        {
            return promotionResult.getOrder();
        }
        return null;
    }


    protected CartService getCartService()
    {
        return this.cartService;
    }


    @Required
    public void setCartService(CartService cartService)
    {
        this.cartService = cartService;
    }
}

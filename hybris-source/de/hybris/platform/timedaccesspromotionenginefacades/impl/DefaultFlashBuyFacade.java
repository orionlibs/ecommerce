package de.hybris.platform.timedaccesspromotionenginefacades.impl;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.timedaccesspromotionenginefacades.FlashBuyFacade;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultFlashBuyFacade implements FlashBuyFacade
{
    private FlashBuyService flashBuyService;
    private ModelService modelService;
    private CartService cartService;


    public String prepareFlashBuyInfo(ProductData product)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        List<FlashBuyCouponModel> flashBuyCouponList = new ArrayList<>();
        Collection<PromotionData> promotionDataList = product.getPotentialPromotions();
        if(CollectionUtils.isNotEmpty(promotionDataList))
        {
            promotionDataList.forEach(p -> getFlashBuyService().getFlashBuyCouponByPromotionCode(p.getCode()).ifPresent(()));
        }
        if(CollectionUtils.isNotEmpty(flashBuyCouponList))
        {
            return ((FlashBuyCouponModel)flashBuyCouponList.get(0)).getCouponId();
        }
        return "";
    }


    public void updateFlashBuyStatusForCart()
    {
        CartModel cart = getCartService().getSessionCart();
        cart.setProcessingFlashBuyOrder(true);
        getModelService().save(cart);
    }


    protected FlashBuyService getFlashBuyService()
    {
        return this.flashBuyService;
    }


    public void setFlashBuyService(FlashBuyService flashBuyService)
    {
        this.flashBuyService = flashBuyService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CartService getCartService()
    {
        return this.cartService;
    }


    public void setCartService(CartService cartService)
    {
        this.cartService = cartService;
    }
}

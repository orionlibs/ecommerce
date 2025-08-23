package de.hybris.platform.promotions.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.OrderPromotion;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.jalo.PromotionGroup;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import java.util.List;

public class AbstractPromotionsService extends AbstractBusinessService
{
    protected OrderPromotion getPromotion(OrderPromotionModel promotion)
    {
        return (OrderPromotion)getModelService().getSource(promotion);
    }


    protected ProductPromotion getPromotion(ProductPromotionModel promotion)
    {
        return (ProductPromotion)getModelService().getSource(promotion);
    }


    protected PromotionGroup getPromotionGroup(PromotionGroupModel group)
    {
        return (PromotionGroup)getModelService().getSource(group);
    }


    protected Order getOrder(OrderModel order)
    {
        return (Order)getModelService().getSource(order);
    }


    protected AbstractOrder getOrder(AbstractOrderModel order)
    {
        return (AbstractOrder)getModelService().getSource(order);
    }


    protected Cart getCart(CartModel cart)
    {
        return (Cart)getModelService().getSource(cart);
    }


    protected Product getProduct(ProductModel product)
    {
        return (Product)getModelService().getSource(product);
    }


    protected SessionContext getSessionContext()
    {
        return JaloSession.getCurrentSession().getSessionContext();
    }


    protected AbstractPromotion getPromotion(AbstractPromotionModel promotion)
    {
        return (AbstractPromotion)getModelService().getSource(promotion);
    }


    protected PromotionResult getResult(PromotionResultModel result)
    {
        return (PromotionResult)getModelService().getSource(result);
    }


    protected void refreshModifiedModelsAfter(List<ItemModel> models)
    {
        for(ItemModel singleModel : models)
        {
            getModelService().refresh(singleModel);
        }
    }
}

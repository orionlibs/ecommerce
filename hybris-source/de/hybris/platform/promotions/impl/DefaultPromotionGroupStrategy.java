package de.hybris.platform.promotions.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.PromotionGroupStrategy;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPromotionGroupStrategy implements PromotionGroupStrategy
{
    private PromotionsService promotionsService;


    public PromotionGroupModel getDefaultPromotionGroup()
    {
        return this.promotionsService.getDefaultPromotionGroup();
    }


    public PromotionGroupModel getDefaultPromotionGroup(AbstractOrderModel order)
    {
        return this.promotionsService.getDefaultPromotionGroup();
    }


    protected PromotionsService getPromotionsService()
    {
        return this.promotionsService;
    }


    @Required
    public void setPromotionsService(PromotionsService promotionsService)
    {
        this.promotionsService = promotionsService;
    }
}

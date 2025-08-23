package de.hybris.platform.promotions;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;

public interface PromotionGroupStrategy
{
    PromotionGroupModel getDefaultPromotionGroup();


    PromotionGroupModel getDefaultPromotionGroup(AbstractOrderModel paramAbstractOrderModel);
}

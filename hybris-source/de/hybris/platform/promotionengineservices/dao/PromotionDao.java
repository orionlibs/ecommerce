package de.hybris.platform.promotionengineservices.dao;

import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;

public interface PromotionDao
{
    AbstractPromotionModel findPromotionByCode(String paramString);


    PromotionGroupModel findPromotionGroupByCode(String paramString);


    PromotionGroupModel findDefaultPromotionGroup();
}

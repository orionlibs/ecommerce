package de.hybris.platform.promotions.attributehandlers;

import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

public class PromotionOrderEntryConsumedOrderEntryNumberAttributeHandler extends AbstractDynamicAttributeHandler<Integer, PromotionOrderEntryConsumedModel>
{
    public Integer get(PromotionOrderEntryConsumedModel model)
    {
        return (model.getOrderEntry() != null) ? model.getOrderEntry().getEntryNumber() : model.getOrderEntryNumber();
    }
}

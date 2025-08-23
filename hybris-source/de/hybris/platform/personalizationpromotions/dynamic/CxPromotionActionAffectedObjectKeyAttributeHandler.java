package de.hybris.platform.personalizationpromotions.dynamic;

import de.hybris.platform.personalizationpromotions.model.CxPromotionActionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class CxPromotionActionAffectedObjectKeyAttributeHandler implements DynamicAttributeHandler<String, CxPromotionActionModel>
{
    public String get(CxPromotionActionModel model)
    {
        return model.getPromotionId();
    }


    public void set(CxPromotionActionModel model, String value)
    {
    }
}

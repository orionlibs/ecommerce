package de.hybris.platform.personalizationservices.dynamic;

import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class CxVariationActiveAttributeHandler implements DynamicAttributeHandler<Boolean, CxVariationModel>
{
    public Boolean get(CxVariationModel model)
    {
        return Boolean.valueOf((model.getStatus() == CxItemStatus.ENABLED && isCustomizationActive(model.getCustomization())));
    }


    protected boolean isCustomizationActive(CxCustomizationModel customization)
    {
        if(customization != null)
        {
            return customization.isActive();
        }
        return true;
    }


    public void set(CxVariationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("This is read only attribute");
    }
}

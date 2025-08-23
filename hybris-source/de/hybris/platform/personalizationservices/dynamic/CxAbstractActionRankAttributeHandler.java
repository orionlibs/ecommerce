package de.hybris.platform.personalizationservices.dynamic;

import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class CxAbstractActionRankAttributeHandler implements DynamicAttributeHandler<Integer, CxAbstractActionModel>
{
    public Integer get(CxAbstractActionModel action)
    {
        CxVariationModel variation = action.getVariation();
        if(variation == null)
        {
            return null;
        }
        return Integer.valueOf(variation.getActions().indexOf(action));
    }


    public void set(CxAbstractActionModel action, Integer value)
    {
        throw new UnsupportedOperationException("This is read only attribute");
    }
}

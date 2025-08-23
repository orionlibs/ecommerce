package de.hybris.platform.personalizationservices.dynamic;

import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class CxAbstractActionAffectedObjectKeyAttributeHandler implements DynamicAttributeHandler<String, CxAbstractActionModel>
{
    public String get(CxAbstractActionModel model)
    {
        return "CxAbstractActionModel";
    }


    public void set(CxAbstractActionModel model, String value)
    {
    }
}

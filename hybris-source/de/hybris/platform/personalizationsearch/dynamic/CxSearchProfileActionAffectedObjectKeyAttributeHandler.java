package de.hybris.platform.personalizationsearch.dynamic;

import de.hybris.platform.personalizationsearch.model.CxSearchProfileActionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class CxSearchProfileActionAffectedObjectKeyAttributeHandler implements DynamicAttributeHandler<String, CxSearchProfileActionModel>
{
    public String get(CxSearchProfileActionModel model)
    {
        return model.getSearchProfileCode();
    }


    public void set(CxSearchProfileActionModel model, String value)
    {
    }
}

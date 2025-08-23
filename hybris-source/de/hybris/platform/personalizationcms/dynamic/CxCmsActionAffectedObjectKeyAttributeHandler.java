package de.hybris.platform.personalizationcms.dynamic;

import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class CxCmsActionAffectedObjectKeyAttributeHandler implements DynamicAttributeHandler<String, CxCmsActionModel>
{
    public String get(CxCmsActionModel model)
    {
        return model.getContainerId() + "_" + model.getContainerId();
    }


    public void set(CxCmsActionModel model, String value)
    {
    }
}

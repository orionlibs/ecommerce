package de.hybris.platform.cms2.model;

import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

public class AbstractRestrictionDescriptionAttributeHandler implements DynamicAttributeHandler<String, AbstractRestrictionModel>
{
    public String get(AbstractRestrictionModel model)
    {
        return "Default description";
    }


    public void set(AbstractRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}

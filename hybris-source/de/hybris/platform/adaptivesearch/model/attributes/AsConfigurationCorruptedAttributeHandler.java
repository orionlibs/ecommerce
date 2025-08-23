package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;

public class AsConfigurationCorruptedAttributeHandler extends AbstractAsAttributeHandler<Boolean, AbstractAsConfigurationModel>
{
    public Boolean get(AbstractAsConfigurationModel model)
    {
        return Boolean.FALSE;
    }


    public void set(AbstractAsConfigurationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("Write is not a valid operation for this dynamic attribute");
    }
}

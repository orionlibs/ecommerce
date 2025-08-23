package de.hybris.platform.servicelayer.model.attribute;

import de.hybris.platform.servicelayer.model.AbstractItemModel;

public abstract class AbstractDynamicAttributeHandler<VALUE, MODEL extends AbstractItemModel> implements DynamicAttributeHandler<VALUE, MODEL>
{
    public VALUE get(MODEL model)
    {
        throw new UnsupportedOperationException();
    }


    public void set(MODEL model, VALUE value)
    {
        throw new UnsupportedOperationException();
    }
}

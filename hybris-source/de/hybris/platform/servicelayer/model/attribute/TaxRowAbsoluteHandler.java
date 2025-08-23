package de.hybris.platform.servicelayer.model.attribute;

import de.hybris.platform.europe1.model.TaxRowModel;

public class TaxRowAbsoluteHandler implements DynamicAttributeHandler<Boolean, TaxRowModel>
{
    public Boolean get(TaxRowModel model)
    {
        return (model.getCurrency() != null) ? Boolean.TRUE : Boolean.FALSE;
    }


    public void set(TaxRowModel model, Boolean aBoolean)
    {
    }
}

package de.hybris.platform.servicelayer.model.attribute;

import de.hybris.platform.europe1.model.AbstractDiscountRowModel;

public class AbstractDiscountRowAbsoluteHandler implements DynamicAttributeHandler<Boolean, AbstractDiscountRowModel>
{
    public Boolean get(AbstractDiscountRowModel model)
    {
        return (model.getCurrency() != null) ? Boolean.TRUE : Boolean.FALSE;
    }


    public void set(AbstractDiscountRowModel model, Boolean aBoolean)
    {
    }
}
